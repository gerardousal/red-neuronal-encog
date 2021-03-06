package nd.nn.stdper;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * Created by driucorado on 11/8/15.
 */
public class BasicNNExample {

    public static double XOR_INPUT[][] = {{0.0, 0.0},
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0}};
    public static double XOR_IDEAL[][] = {{0.0},
            {1.0},
            {1.0},
            {0.0}};

    public static void main(String[] args) {

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        network.getStructure().finalizeStructure();
        network.reset();

        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

        MLTrain train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        do {
            train.iteration();
            System.out.println("Epoch" + epoch + "Error: "
                    + train.getError());
            epoch++;
        } while (train.getError() > 0.01);


        System.out.println("Neural Network Results:");
        for (MLDataPair pair : trainingSet) {
            final MLData output =
                    network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", actual=" + output.getData(0) + ", ideal=" + pair.getIdeal().getData(0));
        }

    }

}
