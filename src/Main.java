import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import GUI.GraphicVisualizer;
import Infrastructure.ImpactHandler;
import Infrastructure.Sloth;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main extends Application implements EventHandler<ActionEvent> {

    private static InputInfo fileData;
    private static int numFilesInSystem = 0;

    /**
     * @param args
     */
    public static void main(String args[]) {
        // inputfile must be passed in as first argument
        String inputFile = args[0];
        try {
            fileData = InputInterpreter.readInput(inputFile);
            fileData.setInputFilePath(args[0]);
            numFilesInSystem = fileData.allSloths.size();
            ImpactHandler impactHandler = new ImpactHandler(fileData);

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

            OutputInterpreter out = new OutputInterpreter(fileData);
            out.generateTxt("output.txt"); //TODO figure out how to use the file path in inputInfo to select the location to save output

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
        int windowHeight = 700;
        int windowWidth = 1300;
        int plotHeight = 115;
        int plotWidth = 110;

        NumberAxis xaxis_chart = new NumberAxis(0, plotHeight, 10);
        NumberAxis yaxis_chart = new NumberAxis(0, plotWidth,10);
        xaxis_chart.setLabel("Percentage of files in system related to file.");
        yaxis_chart.setLabel("Percentage of unique files in total usages.");

        BubbleChart<Number, Number> bubbleChart = new BubbleChart<>(xaxis_chart, yaxis_chart);
        XYChart.Series toPlot;

        GraphicVisualizer gv = new GraphicVisualizer(plotHeight, plotWidth);
//        gv.drawingSettings();
        primaryStage.setTitle("External Dependency Tool");
        StackPane layout = new StackPane();
        BorderPane border = new BorderPane();
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        ObservableList<Pair<Integer, Integer>> coordList = FXCollections.observableList(list);
        ObservableList<Pair<Integer, Integer>> radiiList = FXCollections.observableList(list);

        HashMap<Circle, Sloth> circle2SlothMap = gv.getCircles(fileData.allSloths);


        circle2SlothMap.forEach((circle, sloth) ->
        {


//            layout.getChildren().add(circle);

//            double dx = Math.random()*(height-circle.getRadius());
//            double dy = Math.random()*(width-circle.getRadius());
//            gv.gc.strokeOval(circle.getCenterX()+dx,
//                    circle.getCenterY()+dy,
//                    circle.getRadius(),
//                    circle.getRadius());
        });


        gv.root.getChildren().addAll(gv.canvas, layout);

        toPlot = gv.getDataToPlot(fileData.allSloths);

        StringBuilder toDisplay = new StringBuilder();
        fileData.allSloths.keySet().forEach(s -> toDisplay.append(s + "\n"));
        textArea.setText(toDisplay.toString());


        border.setCenter(bubbleChart);
        border.setBottom(textArea);
        Scene scene = new Scene(border, windowWidth, windowHeight);
        bubbleChart.getData().addAll(toPlot);

//        Scene scene = new Scene(gv.root, width, height);
//        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}