import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import GUI.GraphicVisualizer;
import Infrastructure.Sloth;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application implements EventHandler<ActionEvent>{

    /**
     *
     *
     * @param args
     */
    public static void main(String args[]) {
        // inputfile must be passed in as first argument
        String inputFile = args[0];
        try {
            InputInfo fileLists = InputInterpreter.readInput(inputFile);
            fileLists.setInputFilePath(args[0]);

            OutputInterpreter out = new OutputInterpreter(fileLists);
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
        HashMap<Circle, Sloth> circle2SlothMap = new HashMap<>();


        Scene scene = new Scene(gv.root, width, height);
//        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}