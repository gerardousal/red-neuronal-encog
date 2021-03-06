package nd.nn.stdper.impl.domain;

import java.util.Objects;

/**
 * Created by driucorado on 11/14/15.
 */
public class TrainValue {
    public Double ideal;
    public Double predicted;
    public Double min;
    public Double max;
    public Double error;

    public TrainValue(Double ideal, Double predicted, Double min, Double max, Double error) {
        this.ideal = ideal;
        this.predicted = predicted;
        this.min = min;
        this.max = max;
        this.error = error;
    }

    public boolean isGoodPrediction() {
        if (Objects.equals(ideal, predicted)) {
            return true;
        }
        return false;
    }

    public TrainValue(Double ideal, Double predicted) {
        this.ideal = ideal;
        this.predicted = predicted;
    }
}
