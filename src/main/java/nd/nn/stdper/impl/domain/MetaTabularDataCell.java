package nd.nn.stdper.impl.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by driucorado on 11/10/15.
 */
public class MetaTabularDataCell {
    public MetaTabularDataCell(double max, double min) {
        this.max = max;
        this.min = min;
    }

    public double max = 0;
    public double min = 0;
    private boolean isContinuosValue = false;
    private Set<Double> values = new HashSet<>();

    public int countClass() {
        return values.size();
    }

    public boolean isContinuosValue() {
        return isContinuosValue;
    }

    public void addValue(Double value) {
        if (values.size() <= 10) {
            values.add(value);
        } else {
            isContinuosValue = true;
            values.clear();
        }
    }

}
