package nd.nn.stdper.impl.domain;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.util.*;

/**
 * Created by driucorado on 11/10/15.
 */
public class MetaTabularData {
    public Map<String, MetaTabularDataCell> metaCells = new HashMap<>();
    public List<String> fields = new ArrayList<>();
    public Map<String, Integer> header = new HashMap<>();
    public List<String> inputs = new ArrayList<>();
    public List<String> outputs = new ArrayList<>();
    public Map<String, NormalizedField> metaDataNormalized = new HashMap<>();
    public List<List<Double>> rawData = new ArrayList<>();
    public String fileName;


    public List<MLDataPair> normalized() {
        System.out.println("start normalize analise");
        ;
        List<MLDataPair> mlDataPairs = new ArrayList<>();
        List<String> headers = new ArrayList<>(this.header.keySet());
//        double[][] input = new double[rawData.size()][this.inputs.size()];
//        double[][] output = new double[rawData.size()][this.outputs.size()];
        int countrow = 0;
        for (List<Double> next : rawData) {
            try {
                double[] normalizedVectorInput = new double[next.size() - this.outputs.size()];
                double[] normalizedVectorIdeal = new double[this.outputs.size()];
                int countInput = 0;
                int countIdeal = 0;
                for (int i = 0; i < next.size(); i++) {
                    MetaTabularDataCell metaTabularDataCell = this.metaCells.get(headers.get(i));
                    NormalizedField age = this.getNormalized(headers.get(i), this, metaTabularDataCell.max, metaTabularDataCell.min);
                    double doubleValue = 0;
                    if (metaTabularDataCell.max <= 1 && metaTabularDataCell.min >= 0) {
                        doubleValue = next.get(i);
                    } else {
                        doubleValue = age.normalize(doubleValue);
                    }
                    if (this.inputs.contains(headers.get(i))) {
                        // input[countrow][countInput] = age.normalize(doubleValue);
                        normalizedVectorInput[countInput] = doubleValue;
                                countInput++;
                    }
                    if (this.outputs.contains(headers.get(i))) {
                        // output[countrow][countIdeal] = age.normalize(doubleValue);
                        normalizedVectorIdeal[countIdeal] = doubleValue;
                        countIdeal++;
                    }
                }
                mlDataPairs.add(new BasicMLDataPair(new BasicMLData(normalizedVectorInput), new BasicMLData(normalizedVectorIdeal)));
                countrow++;

            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                throw new Error("Error", ex);
            }
        }
        System.out.println("finish normalize analise");
        return mlDataPairs;
    }

    private NormalizedField getNormalized(String field, MetaTabularData metaTabularData, Double max, Double min) {
        if (!metaTabularData.metaDataNormalized.containsKey(field)) {
            NormalizedField normalizedField = new NormalizedField(
                    NormalizationAction.Normalize, field,
                    max, min, -1, 1);
            metaTabularData.metaDataNormalized.put(field, normalizedField);
            return normalizedField;
        }
        return metaTabularData.metaDataNormalized.get(field);
    }
}
