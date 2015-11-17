package nd.nn.stdper.view;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import nd.nn.stdper.impl.domain.TabularData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by driucorado on 11/8/15.
 */
public class DataWindow {

    Scene scene;
    TableView dataTable;
    Label label;
    TextField inputs;
    TextField targets;
    TableCell selectedTableCell;
    TabularData tabularData;
    Button btnChooseInput  = new Button();
    Button btnChooseOutputs = new Button();

    public DataWindow init(Stage stage) {

        dataTable = new TableView();
        // dataTable.getSelectionModel().setCellSelectionEnabled(true);
        stage.setWidth(300);
        stage.setHeight(500);
        label = new Label("file");
        label.setFont(new Font("Arial", 20));
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, dataTable);
        scene = new Scene(vbox);
        scene.getStylesheets().add("css/app.css");
        vbox.getVgrow(dataTable);
        stage.setScene(scene);
        return this;
    }


    public DataWindow setTabularData(TabularData tabularData) {
        this.tabularData = tabularData;
       // this.setHeaders(new ArrayList<>(tabularData.metaTabularData.header.keySet()));
      //  this.setData(tabularData.data);
        this.setLabelTitle(tabularData.file);
        tabularData.metaTabularData.inputs = new ArrayList<>(tabularData.metaTabularData.header.keySet());
        return this;
    }

    private void setLabelTitle(String file) {
        label.setText(file);
    }


}
