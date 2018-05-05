package GUI;

import Infrastructure.Sloth;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphicVisualizer {

    public Group root;
    public Canvas canvas;
    public GraphicsContext gc;
    private int canvasHeight;
    private int canvasWidth;

    //This is a volatile variable, use only for setting
    private Paint stroke;


    // FROM BUTTON DEMO
//        Button button = new Button("issa button");
//        button.setOnAction(this);
//        StackPane layout = new StackPane();
//        layout.getChildren().add(button);


    public GraphicVisualizer(int canvasHeight, int canvasWidth) {
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.canvas = new Canvas(this.canvasWidth, this.canvasHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.root = new Group();
        this.canvas.setPickOnBounds(true);

    }

//    public void drawingSettings(){
//        this.gc.setStroke(javafx.scene.paint.Color.BLACK);
////        this.gc.strokeOval(100, 400, 30, 30);
//        canvas.setOnMouseClicked((MouseEvent e) -> System.out.println("clicked"));
//
//    }

    // create a circle per sloth and store in a hash map
    public HashMap<Circle, Sloth> getCircles(HashMap<String, Sloth> slothMap) {
        HashMap<Circle, Sloth> circle2SlothMap = new HashMap<>();

        // xscaleFactor, yscaleFactor
        Pair<Float, Float> p = getScaleFactor(slothMap);
        float xscale = p.getKey();
        float yscale = p.getValue();

        for (Sloth sloth : slothMap.values()) {
            if (sloth.immediateUsages == 0) this.stroke = Color.LIGHTBLUE;
            else if (sloth.usageRatio > 50) this.stroke = Color.RED;
            else this.stroke = Color.GRAY;

            // this way every circle has a minimum radius of at least 5
            int radius = Math.round(sloth.immediateUsages + 5);
            int xpos = Math.round(xscale * sloth.impactScore);
            int ypos = Math.round(yscale * sloth.spaghettiScore);

            this.gc.setStroke(this.stroke);
            this.gc.strokeOval(xpos, ypos, radius, radius);

//            //create circle
//            Circle circle = new Circle(xpos, ypos, radius);
//            circle.setStroke(this.stroke);
//            circle.addEventFilter(MouseEvent.MOUSE_CLICKED, onMouseClickedEventHandler);
//            circle2SlothMap.put(circle, sloth);
//        });
        }
        return circle2SlothMap;
    }

    private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // make sure it's the correct (left) mouse button
            if (!event.isSecondaryButtonDown())
                return;

            System.out.println("clicked");
        }
    };

    public static Pair<Float, Float> getScaleFactor(HashMap<String, Sloth> slothMap) {
        float xscale;
        float yscale;

        float xmax = 0;
        float ymax = 0;

        // find max and find 1000/max
        for (Sloth sloth : slothMap.values()) {
            if (xmax < sloth.impactScore) xmax = sloth.impactScore;
            if (ymax < sloth.spaghettiScore) ymax = sloth.spaghettiScore;
        }

        xscale = 200 / xmax;
        yscale = 200 / ymax;

        // use these returned numbers to scale (multiply)  the xpos and ypos variables
        return new Pair<>(xscale, yscale);

    }


    public Pair<XYChart.Series<Number, Number>, XYChart.Series<Number, Number>> getDataToPlot(HashMap<String, Sloth> slothMap, String changed) {
        XYChart.Series<Number, Number> unchangedData = new XYChart.Series<>();
        unchangedData.setName("Unchanged files");
        XYChart.Series<Number, Number> changedData = new XYChart.Series<>();
        changedData.setName("Changed files");

        slothMap.forEach((name, sloth) -> {
            // add all the files that weren't changed
            if (!sloth.fileName.equals(changed)) {
                unchangedData.getData().add(new XYChart.Data<>(sloth.xpos, sloth.ypos, sloth.radius / 10));
            }
            else changedData.getData().add(new XYChart.Data<>(sloth.xpos, sloth.ypos, sloth.radius / 10));


        });

        return new Pair<>(unchangedData, changedData);
    }


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
