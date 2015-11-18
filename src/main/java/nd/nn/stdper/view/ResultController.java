package nd.nn.stdper.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import nd.nn.stdper.impl.domain.TrainResult;
import nd.nn.stdper.impl.domain.TrainValue;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by driucorado on 11/14/15.
 */
public class ResultController implements Initializable {
    @FXML
    GridPane gridError;

    public void setTrainResult(TrainResult trainResult, TrainResult validationResult, List<Double> errors) {
        gridError.getChildren().clear();
        graphError(errors, 0);
        // graphTraining(trainResult);
        graphScatterPoints(trainResult, 1);
        graphScatterPoints(validationResult, 2);
    }

    private void graphTraining(TrainResult trainResult) {
        int count = 1;
        for (Map.Entry<String, List<TrainValue>> result : trainResult.data.entrySet()) {
            TrainValue trainValue1 = result.getValue().get(0);
            final NumberAxis xAxis = new NumberAxis(trainValue1.min - 1, trainValue1.max + 1, 1);
            final NumberAxis yAxis = new NumberAxis(trainValue1.min - 1, trainValue1.max + 1, 1);
            ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);
            xAxis.setLabel("Predicted");
            yAxis.setLabel("Ideal");
            scatterChart.setTitle(result.getKey());
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Data");
            for (TrainValue trainValue : result.getValue()) {
                series1.getData().add(new XYChart.Data(trainValue.predicted, trainValue.ideal));
            }

            scatterChart.getData().add(series1);
            gridError.add(scatterChart, 0, count);
            count++;
        }
    }

    private int graphScatterPoints(TrainResult trainResult, int count) {
        for (Map.Entry<String, List<TrainValue>> result : trainResult.data.entrySet()) {
            TrainValue trainValue1 = result.getValue().get(0);
            final NumberAxis xAxis = new NumberAxis(0, result.getValue().size()+1, 4);
            final NumberAxis yAxis = new NumberAxis(-0.1, 1.1, 0.1);
            ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);
            xAxis.setLabel("Value");
            yAxis.setLabel("Data");
            scatterChart.setTitle(result.getKey() + " " + result.getValue().stream().filter(resultValue -> resultValue.isGoodPrediction()).count() + "/" + result.getValue().size());
            XYChart.Series seriesIdeal = new XYChart.Series();
            XYChart.Series seriesPredicted = new XYChart.Series();
            seriesIdeal.setName("Ideal");
            seriesPredicted.setName("Predicted");
            int countValues = 0;
            for (int i = 0; i < result.getValue().size(); i++) {
                if (i % 5 == 0 || result.getValue().size() < 50) {
                    TrainValue trainValue = result.getValue().get(i);
                    seriesIdeal.getData().add(new XYChart.Data(countValues, trainValue.ideal));
                    seriesPredicted.getData().add(new XYChart.Data(countValues, trainValue.predicted));
                }
                countValues++;
            }

            scatterChart.getData().addAll(seriesIdeal, seriesPredicted);
            gridError.add(scatterChart, 0, count);
            count++;
        }
        return count;
    }

    private int graphError(List<Double> error, int count) {
        long count1 = error.stream().distinct().count();
        double max = error.stream().max((number1, number2) -> Double.compare(number1, number2)).get();
        double min = error.stream().min((number1, number2) -> Double.compare(number1, number2)).get();
        final NumberAxis xAxis = new NumberAxis(0, error.size(), 10);
        final NumberAxis yAxis = new NumberAxis(min, max, 10);
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Error");
        LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Error training");
        for (int i = 0; i < error.size(); i++) {
            if (i % 40 == 0 || error.size() < 100) {
                series.getData().add(new XYChart.Data(i, error.get(i)));
            }
        }
        lineChart.getData().add(series);
        gridError.add(lineChart, 0, count);
        return count;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
