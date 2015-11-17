package nd.nn.stdper.impl.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nd.nn.stdper.impl.domain.TabularData;

import java.io.File;
import java.io.FileReader;


/**
 * Created by driucorado on 11/12/15.
 */
public class LoadFileService extends Service<TabularData> {
    private File file;
    private StringProperty pathFile = new SimpleStringProperty();
    private String delimiter;

    public LoadFileService setFile(final File file) {
        this.file = file;
        return this;
    }

    public LoadFileService setDelimiter(String delimiter) {
        this.delimiter = delimiter; return this;
    }

    @Override
    protected Task<TabularData> createTask() {
        return new Task<TabularData>() {
            @Override
            protected TabularData call() throws Exception {
                pathFile.set(file.getName());
                System.out.println("start analize");
                TabularData tabularData = new TabularDataFactory().analizeFile(file, delimiter, "\"").getTabularData();
                return tabularData;
            }
        };
    }
}
