package nd.nn.stdper.impl.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by driucorado on 11/17/15.
 */
public class ResultData {
    private TrainResult trainResult;
    private TrainResult validationResult;
    private List<Double> trainError = new ArrayList<>();

    public ResultData(TrainResult trainResult, TrainResult validationResult, List<Double> errors) {
        this.trainResult = trainResult;
        this.validationResult = validationResult;
        this.trainError = errors;
    }

    public TrainResult getTrainResult() {
        return trainResult;
    }

    public TrainResult getValidationResult() {
        return validationResult;
    }

    public List<Double> getTrainError() {
        return trainError;
    }
}
