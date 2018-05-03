import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import GUI.GraphicVisualizer;
import Infrastructure.ImpactHandler;
import Infrastructure.Sloth;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application implements EventHandler<ActionEvent> {

    private static InputInfo fileData;
    private static int maxUsedFileTotal = 0;

    /**
     * @param args
     */
    public static void main(String args[]) {
        // inputfile must be passed in as first argument
        String inputFile = args[0];
        try {
            fileData = InputInterpreter.readInput(inputFile);
            fileData.setInputFilePath(args[0]);
            ImpactHandler impactHandler = new ImpactHandler(fileData);
            maxUsedFileTotal = impactHandler.findMaxUsedFileTotal();

            fileData.allSloths.forEach((fileName, sloth) -> {
                Pair<Integer, Integer> p = impactHandler.calCascadingExtDeps(sloth);
                sloth.setTotalUsages(p.getKey());
                sloth.setUniqueUsages(p.getValue());
                sloth.setScores(maxUsedFileTotal);
// TODO                sloth.setCascadeLevels(impactHandler.calcExtDepsLevels(sloth));
            });



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
        int height = 500;
        int width = 500;

        GraphicVisualizer gv = new GraphicVisualizer(height, width);
        gv.drawingSettings();
        primaryStage.setTitle("External Dependency Tool");

//TODO Create circle for each sloth, map sloth to object (object = key, sloth = stored val)
        HashMap<Circle, Sloth> circle2SlothMap = gv.getCircles(fileData.allSloths);
        StackPane layout = new StackPane();

        circle2SlothMap.forEach((circle, sloth) ->
        {
            layout.getChildren().add(circle);

//            double dx = Math.random()*(height-circle.getRadius());
//            double dy = Math.random()*(width-circle.getRadius());
//            gv.gc.strokeOval(circle.getCenterX()+dx,
//                    circle.getCenterY()+dy,
//                    circle.getRadius(),
//                    circle.getRadius());
//            gv.gc.fillText(sloth.getFileName(),
//                    circle.getCenterX()+dx,
//                    circle.getCenterY()+dy,
//                    15);
        });

        gv.root.getChildren().addAll(gv.canvas, layout);

        Scene scene = new Scene(gv.root, width, height);
//        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}