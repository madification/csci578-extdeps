import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;
import Infrastructure.Sloth;
import com.sun.prism.paint.Color;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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
        primaryStage.setTitle("External Dependency Tool");

        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gC = canvas.getGraphicsContext2D();
        gC.setStroke(javafx.scene.paint.Color.BLACK);
        gC.strokeOval(100, 400, 30, 30);
        root.getChildren().add(canvas);


        //TODO Create circle for each sloth, map sloth to object (object = key, sloth = stored val)
        HashMap<Object, Sloth> obj2SlothMap = new HashMap<>();

        Button button = new Button("issa button");
        button.setOnAction(this); //TODO if move gui stuff to another class, replace 'this' with the name of the class::handle
        StackPane layout = new StackPane();

        layout.getChildren().add(button);

        Scene scene = new Scene(root, 500, 500);
//        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}