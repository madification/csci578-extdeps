package GUI;

import Infrastructure.Sloth;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.HashMap;

public class GraphicVisualizer {

    public Group root;
    public Canvas canvas;
    public GraphicsContext gc;
    private int canvasHeight;
    private int canvasWidth;


    public GraphicVisualizer(int canvasHeight, int canvasWidth) {
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.canvas = new Canvas(this.canvasWidth, this.canvasHeight);
        this.gc = canvas.getGraphicsContext2D();
        this.root = new Group();
        this.canvas.setPickOnBounds(true);
    }

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

        xscale = 100 / xmax;
        yscale = 100 / ymax;

        // use these returned numbers to scale (multiply)  the xpos and ypos variables
        return new Pair<>(xscale, yscale);
    }


    public Pair<XYChart.Series<Number, Number>, XYChart.Series<Number, Number>> getDataToPlot(HashMap<String, Sloth> slothMap, String changed) {
        XYChart.Series<Number, Number> unchangedData = new XYChart.Series<>();
        unchangedData.setName("Unchanged files");
        XYChart.Series<Number, Number> changedData = new XYChart.Series<>();
        changedData.setName("Changed files");

        slothMap.forEach((name, sloth) -> {
            // add all the files that weren't changed to unchanged and those that were to changed
            if (!sloth.fileName.equals(changed)) {
                unchangedData.getData().add(new XYChart.Data<>(sloth.xpos, sloth.ypos, sloth.radius / 10));
            }
            else changedData.getData().add(new XYChart.Data<>(sloth.xpos, sloth.ypos, sloth.radius / 10));

        });

        return new Pair<>(unchangedData, changedData);
    }
}
