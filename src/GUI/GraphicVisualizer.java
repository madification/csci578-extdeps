package GUI;

import Infrastructure.Sloth;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class GraphicVisualizer {

    public Group root;
    public Canvas canvas;
    public GraphicsContext gc;




    // FROM BUTTON DEMO
//        Button button = new Button("issa button");
//        button.setOnAction(this); //TODO if move gui stuff to another class, replace 'this' with the name of the class::handle
//        StackPane layout = new StackPane();
//        layout.getChildren().add(button);


    public GraphicVisualizer(int canvasHeight, int canvasWidth) {
        this.canvas = new Canvas(canvasHeight, canvasWidth);
        this.gc = canvas.getGraphicsContext2D();
        this.root = new Group();
        this.canvas.setPickOnBounds(true);

    }

    public void drawingSettings(){
        this.gc.setStroke(javafx.scene.paint.Color.BLACK);
        this.gc.strokeOval(100, 400, 30, 30);
        this.root.getChildren().add(canvas);
                canvas.setOnMouseClicked((MouseEvent e) -> System.out.println("clicked"));

    }
}
