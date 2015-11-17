package nd.nn.stdper.impl.domain;

import nd.nn.stdper.impl.utils.CountTimer;
import org.apache.commons.csv.CSVRecord;

import java.util.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;

/**
 * Created by driucorado on 11/6/15.
 */
public class TabularData {
    public MetaTabularData metaTabularData = new MetaTabularData();
    public List<List<String>> data = new ArrayList<>();
    public String file;

}
