package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.awt.*;
import java.util.LinkedList;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        LinkedList <Point>  points = new Controller().Classify();
        stage.setTitle("Line Chart Sample");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Auc Value: " + getAuc(points));
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("ROC Curve");

        XYChart.Series series = new XYChart.Series();
        series.setName("Lab2");
        for (Point point : points) {
            series.getData().add(new XYChart.Data( point.getX()/100, point.getY()/100));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        stage.setScene(scene);
        stage.show();

    }

    private double getAuc(LinkedList<Point> points) {
        double result = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            Point curr = points.get(i);
            Point next = points.get(i + 1);

            result += Math.abs(curr.getX() - next.getX()) * (curr.getY() + next.getY()) / (2 * 10000);
        }
        return result;
    }

}
