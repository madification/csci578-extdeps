import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application{

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
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("External Dependency Tool");

        Button button = new Button("issa button");
        StackPane layout = new StackPane();

        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}