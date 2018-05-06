import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import GUI.GraphicVisualizer;
import Infrastructure.ImpactHandler;
import Infrastructure.Sloth;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Main extends Application implements EventHandler<ActionEvent> {

    private static InputInfo fileData;
    private static int numFilesInSystem = 0;

    private static int WINDOW_HEIGHT = 730;
    private static int WINDOW_WIDTH = 1000;
    private static int PLOT_HEIGHT = 615;
    private static int PLOT_WIDTH = 1000;
    private static int LIST_HEIGHT = 115;
    private static int LIST_WIDTH = 1000;
    private static String TITLE = "Potential Impact on System of Change to Each File";
    private static String lastSelected = "";


    private static Pane chartPane;
    private static GraphicVisualizer gv = new GraphicVisualizer(WINDOW_HEIGHT, WINDOW_WIDTH);

    /**
     * @param args
     */
    public static void main(String args[]) {
        // inputfile must be passed in as first argument
        String inputFile = args[0];
        try {
            // read the input file and extract "sloth" objects which represent individual files
            fileData = InputInterpreter.readInput(inputFile);
            fileData.setInputFilePath(args[0]);
            numFilesInSystem = fileData.allSloths.size();
            ImpactHandler impactHandler = new ImpactHandler(fileData);

            // extract desired information for each sloth concerning usages
            fileData.allSloths.forEach((fileName, sloth) -> {
                // Get totalUsages and uniqueUsages for each sloth
                Pair<Float, Float> p = impactHandler.calCascadingExtDeps(sloth);
                sloth.setTotalUsages(p.getKey());
                sloth.setUniqueUsages(p.getValue());
            });

            //now that we've gotten scores for all sloths, let's find which has the most relations/usages/nodes in it's tree
            impactHandler.findMaxUsedFileTotal();

            // now we have all the scores set for each sloth and know what the largest number of usages is
            // so lets calculate the impact scores for each sloth
            fileData.allSloths.forEach((fileName, sloth) -> sloth.setScores(impactHandler.getMaxUsageTotal(), numFilesInSystem));

            // xscaleFactor, yscaleFactor for plotting
            Pair<Float, Float> scaleFactor = GraphicVisualizer.getScaleFactor(fileData.allSloths);
            fileData.allSloths.forEach((name, sloth) -> sloth.prepareForPlotting(scaleFactor.getKey(), scaleFactor.getValue()));

            // write our output describing file usages
            OutputInterpreter out = new OutputInterpreter(fileData);
            out.generateUsageText(inputFile);
            // write output of files sorted by scores
            impactHandler.getSortedScoreLists();
            out.generateSortedListsText(inputFile, impactHandler.getImmediateUsageList(), impactHandler.getImpactList());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        launch(new String[]{});
    }

    @Override
    public void handle(ActionEvent event) {
        event.getSource();


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("External Dependency Tool");
        BorderPane border = new BorderPane();
        chartPane = new Pane();
        chartPane.getChildren().clear();
        chartPane.getChildren().addAll(getChart("xxx"));
        border.setCenter(chartPane);


        Pane textPane = new Pane();
        textPane.getChildren().add(getTextToDisplay());
        border.setBottom(textPane);


        Scene scene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public BubbleChart<Number, Number> getChart(String changed) {
        final int xplotAxis = 115;
        final int yplotAxis = 110;

        // set up axes
        final NumberAxis xaxis_chart = new NumberAxis(0, 110, 1000);
        xaxis_chart.setLabel("Percentage of files in system related to file.");
        xaxis_chart.setAutoRanging(false);
        final NumberAxis yaxis_chart = new NumberAxis(0, 110, 100);
        yaxis_chart.setLabel("Percentage of unique files in total usages.");
        yaxis_chart.setAutoRanging(false);


        // create bubble chart
        final BubbleChart<Number, Number> chart = new BubbleChart<>(xaxis_chart, yaxis_chart);
        chart.setTitle(TITLE);
        chart.setPrefSize(PLOT_WIDTH, PLOT_HEIGHT);
        // extract the data to add to the bubble chart


        if (!changed.equals("xxx")) {
            // changed is the index of the file that was changed

            // get a series of unchanged data then changed data
            Pair<XYChart.Series<Number, Number>, XYChart.Series<Number, Number>> toPlot = gv.getDataToPlot(fileData.allSloths, changed);

            // pull out the unchanged/nonselected files
            XYChart.Series<Number, Number> nonselectedFiles = toPlot.getKey();
            nonselectedFiles.setName("Files");

            // place all the files which weren't changed as one series
//            chart.getData().add(nonselectedFiles);

            // pull out the changed/selected files
            XYChart.Series<Number, Number> selectedFiles = toPlot.getValue();
            selectedFiles.setName(changed);


            // place changed/selected file(s) as second series
            chart.getData().add(selectedFiles);
//            chart.getData().addAll(nonselectedFiles, selectedFiles);
        }
        else{
            Pair<XYChart.Series<Number, Number>, XYChart.Series<Number, Number>> toPlot = gv.getDataToPlot(fileData.allSloths, null);
            XYChart.Series<Number, Number> series = toPlot.getKey();
            series.setName("Radius = # direct usages in system");
            chart.getData().add(series);
        }

        return chart;
    }

    public ListView<String> getTextToDisplay() {
        List<String> slothNameList = new ArrayList<>();
        slothNameList.addAll(fileData.allSloths.keySet());
        // sort the list and add it to the list view so it will display alphabetically
        Collections.sort(slothNameList);
        ListView<String> listView = new ListView<>(FXCollections.observableList(slothNameList));

        listView.setPrefSize(LIST_WIDTH, LIST_HEIGHT);
        listView.setEditable(false);

        // event handler for click in ListView
        listView.setOnMouseClicked(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();

            for (int i = 0; i < slothNameList.size(); i++)
            {
                if (slothNameList.get(i).equals(selected))
                {
                    if(lastSelected.equals(selected))
                    {
                        lastSelected = "";
                        chartPane.getChildren().clear();
                        chartPane.getChildren().add(getChart("xxx"));
                    }
                    else
                    {
                        lastSelected = selected;
                        chartPane.getChildren().clear();
                        chartPane.getChildren().addAll(getChart(selected), getDisplayButton(selected));
                    }

                    break;

                }
            }
        });

        return listView;
    }

    /**
     * This method returns the displayButton which removes the chart and adds the text area of file details
     * It adds the return button in addition to the list view.
     * @param fileName file selected from list view event handler
     * @return displayButton
     */
    public Button getDisplayButton(String fileName) {
        Button displayBtn = new Button("Show Details");
        displayBtn.relocate(10, 575);

        displayBtn.setOnMouseClicked(event -> {
            chartPane.getChildren().clear();
            chartPane.getChildren().addAll(getReturnButton(), getFileDetails(fileName));

        });

        return displayBtn;
    }

    public Button getReturnButton(){
        Button returnButton = new Button("Show Graph");
        returnButton.relocate(10, 575);

        returnButton.setOnMouseClicked(event -> {
            chartPane.getChildren().clear();
            chartPane.getChildren().addAll(getChart("xxx"));
        });
        return returnButton;
    }



    // this will be called by the event handler for the button click
    public TextArea getFileDetails(String fileName){
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(PLOT_WIDTH, PLOT_HEIGHT-100);
        textArea.setScrollTop(0);


        Sloth sloth = fileData.allSloths.get(fileName);
        textArea.setText("Showing details for:    " + fileName + "\n" + "\n");
        // immediate usages
        textArea.appendText("Number of Direct External Dependencies: " + "\n" + "       " + sloth.immediateUsages + "\n");
        textArea.appendText("This is the number of direct references to this file by other files or, in other words, " +
                "the number of files in the system which depend on this file." + "\n");
        textArea.appendText("Think of this as the children of the file node." + "\n" + "\n");
        // unique usages
        textArea.appendText("Number of Unique Cascading External Dependencies: " + "\n" + "       " + sloth.uniqueUsages + "\n");
        textArea.appendText("This is the number of unique files which reference this file both directly and indirectly." + "\n");
        textArea.appendText("Think of this as the number of unique nodes in the file's tree." + "\n" + "\n");
        // total usages
        textArea.appendText("Total Number of Cascading External Dependencies: " + "\n" + "       " + sloth.totalUsages + "\n");
        textArea.appendText("This is the total number of files which reference this file directly or indirectly." + "\n");
        textArea.appendText("Think of this as the total number of nodes in the file's tree or as the number of nodes " +
                "in the system's tree which can be traced back to the file node." + "\n" + "\n");
        // spaghetti factor
        textArea.appendText("Spaghetti Factor: " + "\n" + "       " + sloth.spaghettiScore + "\n");
        textArea.appendText("This is the percentage of the total cascading references to this file which are unique." + "\n");
        textArea.appendText("This shows us how interconnected the file references are. A percentage of 0 means every " +
                "reference to this file was unique." + "\n" +
                "A factor greater than 0 means of there are interconnected dependencies amongst the total " +
                "references to this file. Thus creating a spaghetti like set of inter-dependencies." + "\n" + "\n");
        // impact score
        textArea.appendText("Impact Score: " + "\n" + "       " + sloth.impactScore+ "%" + "\n");
        textArea.appendText("This is the percentage of unique files within the whole system which reference this file " +
                "directly and indirectly." + "\n" + "Think of this as the percentage of unique nodes in the system's tree " +
                "which can be traced back to the file node." +"\n" + "\n");
        // list of external dependencies
        textArea.appendText("\n" + "Listed below are all system files which directly reference this file" + "\n");
        sloth.extDepsList.forEach(file ->  textArea.appendText("       " + file + "\n"));



        return textArea;
    }

}