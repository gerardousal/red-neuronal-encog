package nd.nn.stdper;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import nd.nn.stdper.impl.domain.MetaTabularData;
import org.encog.ml.data.MLDataPair;

import java.util.List;


/**
 * Created by driucorado on 11/6/15.
 */
public class TableViewExample extends Application {
    private TableView table = new TableView();

    public static void main(String[] args) {
        launch(args);
    }

    public void config(MetaTabularData metaTabularData, Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle(metaTabularData.fileName);
        stage.setWidth(300);
        stage.setHeight(500);
        List<String> fields = metaTabularData.fields;
        final Label label = new Label(metaTabularData.fileName);
        label.setFont(new Font("Arial", 20));
        table.setEditable(false);
        for (String field : fields) {
            TableColumn firstNameCol = new TableColumn(field);
            table.getColumns().add(firstNameCol);
        }
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public void config(MetaTabularData metaTabularData) {

        List<String> fields = metaTabularData.fields;
        for (int i = 0; i <= fields.size(); i++) {
            table.getColumns().addAll(getTableColumn(fields.get(i), i));
        }
        final Label label = new Label(metaTabularData.fileName);
        label.setFont(new Font("Arial", 20));
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
    }

    public void data(List<MLDataPair> mlDataPairs)
    {
        
    }

    @Override
    public void start(Stage stage) {

    }

    public TableColumn<ObservableList<String>, String> getTableColumn(String name, int index) {
        TableColumn<ObservableList<String>, String> tableColumn = new TableColumn<>(name);
        tableColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().get(index)));
        return tableColumn;
    }
}
