package nd.nn.stdper.impl.domain;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by driucorado on 11/14/15.
 */
public class InputColumn {
    private final SimpleStringProperty name;
    private final SimpleBooleanProperty isInput;
    private final SimpleBooleanProperty isOutput;


    public InputColumn(SimpleStringProperty name, SimpleBooleanProperty isInput, SimpleBooleanProperty isOutput) {
        this.name = name;
        this.isInput = isInput;
        this.isOutput = isOutput;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getIsInput() {
        return isInput.get();
    }

    public SimpleBooleanProperty isInputProperty() {
        return isInput;
    }

    public void setIsInput(boolean isInput) {
        this.isInput.set(isInput);
    }

    public boolean getIsOutput() {
        return isOutput.get();
    }

    public SimpleBooleanProperty isOutputProperty() {
        return isOutput;
    }

    public void setIsOutput(boolean isOutput) {
        this.isOutput.set(isOutput);
    }
}
