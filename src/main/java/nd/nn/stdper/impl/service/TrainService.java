package nd.nn.stdper.impl.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nd.nn.stdper.impl.domain.*;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.error.ErrorCalculation;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.NormalizedField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by driucorado on 11/14/15.
 */
public class TrainService extends Service<ResultData> {
    private TabularData tabularData;
    private double trainPercentage;
    private NeuralNetworkConfigData neuralNetworkConfigData;

    public void setTabularData(TabularData tabularData) {
        this.tabularData = tabularData;
    }

    public void setTrainPercentage(double trainPercentage) {
        this.trainPercentage = trainPercentage;
    }

    public void setNeuralNetworkConfigData(NeuralNetworkConfigData neuralNetworkConfigData) {
        this.neuralNetworkConfigData = neuralNetworkConfigData;
    }

    public static double XOR_INPUT[][] = {{0.0, 0.0},
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0}};
    public static double XOR_IDEAL[][] = {{0.0},
            {1.0},
            {1.0},
            {0.0}};

    @Override
    protected Task<ResultData> createTask() {
        return new Task<ResultData>() {
            @Override
            protected ResultData call() throws Exception {
                return train(tabularData.metaTabularData.normalized(), neuralNetworkConfigData);
            }
        };
    }

    private BasicNetwork getNetwork(NeuralNetworkConfigData networkConfigData) {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, false, tabularData.metaTabularData.inputs.size()));
        for (NeuralNetworkLayerData neuralNetworkLayerData : networkConfigData.layers) {
            network.addLayer(new BasicLayer(neuralNetworkLayerData.activationFunction, neuralNetworkLayerData.bias, neuralNetworkLayerData.count));
        }
        network.addLayer(new BasicLayer(networkConfigData.function, false, tabularData.metaTabularData.outputs.size()));
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }

    private ResultData train(List<MLDataPair> mlDataPairs, NeuralNetworkConfigData networkConfigData) {
        System.out.println("start training");
        int trainCount = (int) (trainPercentage * mlDataPairs.size() / 100);
        BasicMLDataSet trainSet = new BasicMLDataSet(mlDataPairs.subList(0, trainCount));
        BasicMLDataSet validationSet = new BasicMLDataSet(mlDataPairs.subList(0, mlDataPairs.size() - trainCount));
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        network.getStructure().finalizeStructure();
        network.reset();
        /*
        BasicMLDataSet trainSet = new BasicMLDataSet(mlDataPairs.subList(0, trainCount));
        BasicNetwork network = getNetwork(networkConfigData);
        MLTrain train = new Backpropagation(network, trainSet);
        ErrorCalculation errorCalculation = new ErrorCalculation();
        for (int i = 0; i < 500; i++) {
            train.iteration();
        }
        TrainResult trainResult = new TrainResult(tabularData.metaTabularData.outputs);
         */
        //MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
        MLTrain train = new ResilientPropagation(network, trainSet);
        ErrorCalculation errorCalculation = new ErrorCalculation();
        int epoch = 1;
        List<Double> trainError = new ArrayList<>();
        boolean stop = false;
        do {
            train.iteration();
            System.out.println("Epoch" + epoch + "Error: "
                    + train.getError());
            epoch++;
            trainError.add(train.getError());
            if (epoch == 50) {
                stop = true;
            }
        } while (train.getError() > 0.01 && !stop);

        //set de entrenamiento
        TrainResult trainResult = fillTrainResult(trainSet, network);
        //set validacio
        // TrainResult validationResult = fillTrainResult(data, network);
        System.out.println("finish training");
        return new ResultData(trainResult, null, trainError);
    }

    private TrainResult fillTrainResult(MLDataSet set, BasicNetwork network) {
        TrainResult trainResult = new TrainResult(tabularData.metaTabularData.outputs);
        ErrorCalculation errorCalculation = new ErrorCalculation();
        for (MLDataPair pair : set) {
            final MLData output = network.compute(pair.getInput());
            errorCalculation.updateError(pair.getIdeal().getData(), output.getData(), pair.getSignificance());
            double calculate = errorCalculation.calculate();
            trainResult.error = calculate * 100;
            for (int i = 0; i < tabularData.metaTabularData.outputs.size(); i++) {
                List<TrainValue> trainValues = trainResult.data.get(tabularData.metaTabularData.outputs.get(i));
                String columnName = tabularData.metaTabularData.outputs.get(i);
                MetaTabularDataCell metaTabularDataCell = tabularData.metaTabularData.metaCells.get(columnName);
                double predicted = 0;
                double ideal = 0;
                if (metaTabularDataCell.max <= 1 && metaTabularDataCell.min >= 0) {
                    ideal = pair.getIdeal().getData(i);
                    predicted = output.getData(i);
                } else {
                    ideal = tabularData.metaTabularData.metaDataNormalized.get(columnName).deNormalize(pair.getIdeal().getData(i));
                    predicted = tabularData.metaTabularData.metaDataNormalized.get(columnName).deNormalize(output.getData(i));
                }
                //double ideal = tabularData.metaTabularData.metaDataNormalized.get(columnName).deNormalize(pair.getIdeal().getData(i));
                trainValues.add(new TrainValue(ideal, predicted, metaTabularDataCell.min, metaTabularDataCell.max, calculate));
            }
        }
        return trainResult;
    }

    private Double filter(Double predicted, MetaTabularDataCell metaTabularDataCell, NormalizedField normalizedField) {
        double normalValue = normalizedField.deNormalize(predicted);
//        if (metaTabularDataCell.isContinuosValue()) {
//            return predicted;
//        } else {
//            if (metaTabularDataCell.countClass() == 2) {
//                if (predicted <= 0.5) {
//                    return 0.0;
//                } else {
//                    return 1.0;
//                }
//            }
//        }
        return normalValue;
    }
}


