package nd.nn.stdper;


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nd.nn.stdper.impl.domain.MetaTabularData;
import nd.nn.stdper.impl.domain.MetaTabularDataCell;
import nd.nn.stdper.view.MainWindow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.mathutil.error.ErrorCalculation;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by driucorado on 11/5/15.
 */
public class Main extends Application {

    MainWindow mainWindow;

    public void hello() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.newFormat(',').withHeader().withQuote('\"');
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        FileReader reader = new FileReader(file);
        CSVParser csvParser = new CSVParser(reader, csvFormat);
        Iterator<CSVRecord> iteratorMax = csvParser.iterator();

        MetaTabularData metaTabularData = new MetaTabularData();

        //allfields
        metaTabularData.fields.add("a1");
        metaTabularData.fields.add("b1");
        metaTabularData.fields.add("r1");

        //fields for training
        metaTabularData.inputs.add("a1");
        metaTabularData.inputs.add("b1");
        metaTabularData.outputs.add("r1");

        metaTabularData.fileName = file.getName();
        //obtener los datos
        List<List<Double>> rawData = new ArrayList<>();
        while (iteratorMax.hasNext()) {
            CSVRecord next = iteratorMax.next();
            List<Double> data = new ArrayList<>();
            for (String field : metaTabularData.fields) {
                double age = Double.parseDouble(next.get(field));
                if (!metaTabularData.metaCells.containsKey(field)) {
                    metaTabularData.metaCells.put(field, new MetaTabularDataCell(0, Double.MAX_VALUE));
                }
                if (age > metaTabularData.metaCells.get(field).max)
                    metaTabularData.metaCells.put(field, new MetaTabularDataCell(age, metaTabularData.metaCells.get(field).min));
                if (age <= metaTabularData.metaCells.get(field).min)
                    metaTabularData.metaCells.put(field, new MetaTabularDataCell(metaTabularData.metaCells.get(field).max, age));
                data.add(age);
            }
            rawData.add(data);
        }
        //normalizar
        Iterator<List<Double>> iterator1 = rawData.iterator();
        List<MLDataPair> mlDataPairs = new ArrayList<>();
        while (iterator1.hasNext()) {
            List<Double> next = iterator1.next();

            double[] normalizedVectorInput = new double[next.size() - metaTabularData.outputs.size()];
            double[] normalizedVectorIdeal = new double[metaTabularData.outputs.size()];
            int countInput = 0;
            int countIdeal = 0;
            for (int i = 0; i < next.size(); i++) {
                MetaTabularDataCell metaTabularDataCell = metaTabularData.metaCells.get(metaTabularData.fields.get(i));
                NormalizedField age = getNormalized(metaTabularData.fields.get(i), metaTabularData, metaTabularDataCell.max, metaTabularDataCell.min);
                double doubleValue = next.get(i);
                if (metaTabularData.inputs.contains(metaTabularData.fields.get(i))) {
                    normalizedVectorInput[countInput] = age.normalize(doubleValue);
                    countInput++;
                }
                if (metaTabularData.outputs.contains(metaTabularData.fields.get(i))) {
                    normalizedVectorIdeal[countIdeal] = age.normalize(doubleValue);
                    countIdeal++;
                }
            }
            mlDataPairs.add(new BasicMLDataPair(new BasicMLData(normalizedVectorInput), new BasicMLData(normalizedVectorIdeal)));
        }


        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
        network.getStructure().finalizeStructure();
        network.reset();

      MLDataSet trainingSet = new BasicMLDataSet(mlDataPairs);

        MLTrain train = new ResilientPropagation(network, trainingSet);
        ErrorCalculation errorCalculation = new ErrorCalculation();
        int epoch = 1;
        for (int i = 0; i < 500; i++) {
            train.iteration();
        }
        double error = 0;
        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            errorCalculation.updateError(pair.getIdeal().getData(), output.getData(), pair.getSignificance());
            double calculate = errorCalculation.calculate();
            error = calculate * 100;
            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", actual=" + output.getData(0)
                    + ", ideal=" + pair.getIdeal().getData(0)
                    + " | "
                    + "desnormalized ideal=" + metaTabularData.metaDataNormalized.get("r1").deNormalize(pair.getIdeal().getData(0))
                    + " actual=" + metaTabularData.metaDataNormalized.get("r1").deNormalize(output.getData(0))
                    + " error: " + error);
        }
        System.out.println("END Error: " + error);
    }

    public NormalizedField getNormalized(String field, MetaTabularData metaTabularData, Double max, Double min) {
        if (!metaTabularData.metaDataNormalized.containsKey(field)) {
            NormalizedField normalizedField = new NormalizedField(
                    NormalizationAction.Normalize, field,
                    max, min, -0.9, 0.9);
            metaTabularData.metaDataNormalized.put(field, normalizedField);
            return normalizedField;
        }
        return metaTabularData.metaDataNormalized.get(field);

    }
}
