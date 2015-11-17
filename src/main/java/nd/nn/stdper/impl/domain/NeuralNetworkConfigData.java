package nd.nn.stdper.impl.domain;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by driucorado on 11/14/15.
 */
public class NeuralNetworkConfigData {
        public List<NeuralNetworkLayerData> layers = new ArrayList<>();
        public ActivationFunction function;


    public static NeuralNetworkConfigData defaultNeuralNetworkConfigData(Integer countNeurons)
    {
        NeuralNetworkConfigData neuralNetworkConfigData = new NeuralNetworkConfigData();
        neuralNetworkConfigData.function = new ActivationSigmoid();
        neuralNetworkConfigData.layers.add(new NeuralNetworkLayerData(new ActivationSigmoid(), countNeurons));
        return neuralNetworkConfigData;
    }
}
