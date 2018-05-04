package GUI;

import Infrastructure.Sloth;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
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
//        this.gc.strokeOval(100, 400, 30, 30);
        canvas.setOnMouseClicked((MouseEvent e) -> System.out.println("clicked"));

    }

    public HashMap<Circle, Sloth> getCircles(HashMap<String, Sloth> slothMap){
        HashMap<Circle, Sloth> circle2SlothMap = new HashMap<>();

        slothMap.forEach((fileName, sloth) -> {
            //create circle
            Circle circle = new Circle(sloth.uniqueUsages);
            circle.addEventFilter(MouseEvent.MOUSE_CLICKED, onMouseClickedEventHandler);
            circle.relocate(sloth.impactScore, sloth.impactScore); //TODO put in proper coordinates; impactProbability and cascadeLevel?
            circle2SlothMap.put(circle, sloth);
        });

        return circle2SlothMap;
    }

    private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // make sure it's the correct (left) mouse button
            if( !event.isSecondaryButtonDown())
                return;

            System.out.println("clicked");

        }

    };

//TODO CLEAN UP BELOW



//    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
//
//        public void handle(MouseEvent event) {
//
//            // right mouse button => panning
//            if( !event.isSecondaryButtonDown())
//                return;
//
//            sceneDragContext.mouseAnchorX = event.getSceneX();
//            sceneDragContext.mouseAnchorY = event.getSceneY();
//
//            sceneDragContext.translateAnchorX = canvas.getTranslateX();
//            sceneDragContext.translateAnchorY = canvas.getTranslateY();
//
//        }
//
//    };
//
//
//
//    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
//        public void handle(MouseEvent event) {
//
//            // right mouse button => panning
//            if( !event.isSecondaryButtonDown())
//                return;
//
//            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
//            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
//
//            event.consume();
//        }
//    };
//
//    /**
//     * Mouse wheel handler: zoom to pivot point
//     */
//    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
//
//        @Override
//        public void handle(ScrollEvent event) {
//
//            double delta = 1.2;
//
//            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
//            double oldScale = scale;
//
//            if (event.getDeltaY() < 0)
//                scale /= delta;
//            else
//                scale *= delta;
//
//            scale = clamp( scale, MIN_SCALE, MAX_SCALE);
//
//            double f = (scale / oldScale)-1;
//
//            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
//            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));
//
//            canvas.setScale( scale);
//
//            // note: pivot value must be untransformed, i. e. without scaling
//            canvas.setPivot(f*dx, f*dy);
//
//            event.consume();
//
//        }
//
//    };
//
//
//    public static double clamp( double value, double min, double max) {
//
//        if( Double.compare(value, min) < 0)
//            return min;
//
//        if( Double.compare(value, max) > 0)
//            return max;
//
//        return value;
//    }
//}




}
