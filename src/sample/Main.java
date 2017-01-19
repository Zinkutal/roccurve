package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.LinkedList;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        LinkedList <javafx.util.Pair<Double, Double>>  points = new Controller().Classify();
        stage.setTitle("Line Chart Sample");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Auc Value: " + getAuc(points));
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("ROC Curve");

        XYChart.Series series = new XYChart.Series();
        series.setName("Lab2");
        for (Pair<Double, Double> point : points) {
            series.getData().add(new XYChart.Data(point.getKey(), point.getValue()));
            //System.out.println("x=" + point.getKey() + "; y=" + point.getValue());
        }


        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.Y_AXIS);
        stage.setScene(scene);
        stage.show();

    }

    private double getAuc(LinkedList<javafx.util.Pair<Double, Double>> points) {
        double result = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            javafx.util.Pair<Double, Double> curr = points.get(i);
            javafx.util.Pair<Double, Double> next = points.get(i + 1);

            result += Math.abs(curr.getKey() - next.getKey()) * (curr.getValue() + next.getValue()) / (2);
        }
        return result;
    }

}
