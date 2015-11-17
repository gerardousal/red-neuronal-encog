package nd.nn.stdper.impl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by driucorado on 11/14/15.
 */
public class TrainResult {
    public Map<String, List<TrainValue>> data = new HashMap<>();
    public double error;

    public TrainResult(List<String> outputs) {
        for (String output : outputs) {
            data.put(output, new ArrayList<>());
        }
    }
}
