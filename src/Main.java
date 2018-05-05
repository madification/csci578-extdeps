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
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        border.setTop(chartPane);


        Pane textPane = new Pane();
        textPane.getChildren().add(getTextToDisplay(fileData.allSloths));
        border.setBottom(textPane);


//        StackPane layout = new StackPane();


//        bubbleChart = getChart("xxx", fileData.allSloths);
//        bubbleChart.setPrefSize(PLOT_WIDTH, PLOT_HEIGHT);
//        ListView<String> listView =
//        listView.setPrefSize(LIST_WIDTH, LIST_HEIGHT);
//
//        chartPane.getChildren().add(bubbleChart);



        Scene scene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);

//        Scene scene = new Scene(gv.root, width, height);
//        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public ListView<String> getTextToDisplay(HashMap<String, Sloth> slothMap) {
        List<String> slothNameList = new ArrayList<>();
        slothNameList.addAll(slothMap.keySet());
        ListView<String> listView = new ListView<>(FXCollections.observableList(slothNameList));
        listView.setPrefSize(LIST_WIDTH, LIST_HEIGHT);
        listView.setEditable(false);
        listView.setOnMouseClicked(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            System.out.println("clicked " + selected);

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
                        chartPane.getChildren().add(getChart(selected));
                    }

                    break;

                }
            }
        });

        return listView;
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
            selectedFiles.setName("Selected File");


            // place changed/selected file(s) as second series
            chart.getData().add(selectedFiles);
//            chart.getData().addAll(nonselectedFiles, selectedFiles);
        }
        else{
            Pair<XYChart.Series<Number, Number>, XYChart.Series<Number, Number>> toPlot = gv.getDataToPlot(fileData.allSloths, null);
            XYChart.Series<Number, Number> series = toPlot.getKey();
            series.setName("Radius = % usage in system");
            chart.getData().add(series);
        }

        return chart;
    }
}