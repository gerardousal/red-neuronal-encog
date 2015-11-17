package nd.nn.stdper.impl.domain;

import org.encog.engine.network.activation.ActivationFunction;

/**
 * Created by driucorado on 11/14/15.
 */
public class NeuralNetworkLayerData {
    public ActivationFunction activationFunction;
    public Integer count;
    public boolean bias;

    public NeuralNetworkLayerData(ActivationFunction activationFunction, Integer count) {
        this.activationFunction = activationFunction;
        this.count = count;
        this.bias = true;
    }
}
