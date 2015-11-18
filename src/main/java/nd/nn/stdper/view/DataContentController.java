package nd.nn.stdper.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import nd.nn.stdper.impl.domain.*;
import nd.nn.stdper.impl.service.TrainService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by driucorado on 11/13/15.
 */
public class DataContentController implements Initializable {
    @FXML
    Button btnTrain;
    @FXML
    TextField txtTrain;
    @FXML
    TableView dataTable;
    @FXML
    TableView columnTable;
    @FXML
    TextField txtHidden;

    TrainService trainService;
    ResultController resultController;

    private TabularData tabularData;




    public void setTabularData(TabularData tabularData) {
        dataTable.getItems().clear();
        this.tabularData = tabularData;
        ArrayList<String> columns = new ArrayList<>(tabularData.metaTabularData.header.keySet());
        setHeaders(columns);
        setData(tabularData.data);

        final ObservableList<InputColumn> data = FXCollections.observableArrayList();
        for (String column : columns) {
            InputColumn inputColumn = new InputColumn(new SimpleStringProperty(column), new SimpleBooleanProperty(true), new SimpleBooleanProperty(false));
            data.addAll(inputColumn);
        }
        InputColumn lastInputColumn = data.get(data.size() - 1);
        lastInputColumn.setIsInput(false);
        lastInputColumn.setIsOutput(true);

        columnTable.getItems().addAll(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trainService = new TrainService();
        txtTrain.setText("50");
        txtHidden.setText("20");
        configColumnTable();
        AnchorPane resultPane = getResultPane();
        Stage resultStage = new Stage();
        resultStage.setScene(new Scene(resultPane));
        btnTrain.setOnMouseClicked(event -> {
            trainService.reset();
            ObservableList<InputColumn> inputColumns = columnTable.getItems();
            tabularData.metaTabularData.inputs = inputColumns.stream().filter(input -> input.getIsInput()).map(input -> input.getName()).collect(Collectors.toList());
            tabularData.metaTabularData.outputs = inputColumns.stream().filter(input -> input.getIsOutput()).map(input -> input.getName()).collect(Collectors.toList());
            System.out.println("input s: " + tabularData.metaTabularData.inputs.size() + " d:"+Arrays.toString(tabularData.metaTabularData.inputs.toArray()));
            System.out.println("output s: " + tabularData.metaTabularData.outputs.size() + " d:"+Arrays.toString(tabularData.metaTabularData.outputs.toArray()));
            trainService.setTabularData(tabularData);
            trainService.setTrainPercentage(Double.parseDouble(txtTrain.getText()));
            trainService.setNeuralNetworkConfigData(NeuralNetworkConfigData.defaultNeuralNetworkConfigData(Integer.valueOf(txtHidden.getText())));
            trainService.start();
        });
        trainService.setOnSucceeded(event -> {
            ResultData trainResult = (ResultData) event.getSource().getValue();
            resultController.setTrainResult(trainResult.getTrainResult(), trainResult.getValidationResult(), trainResult.getTrainError());
            resultStage.show();
        });
    }

    private AnchorPane getResultPane()
    {
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/fxml/errorGraph.fxml"));
        try {
            AnchorPane anchorPane = fxLoader.load();
            resultController = (ResultController) fxLoader.getController();
            return anchorPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void configColumnTable()
    {
        TableColumn columnName = new TableColumn("Column");
        TableColumn input = new TableColumn("isInput");
        TableColumn output = new TableColumn("isOutput");
        columnName.setMinWidth(100);
        columnName.setCellValueFactory(
                new PropertyValueFactory<InputColumn, String>("name"));
        input.setCellValueFactory(
                new PropertyValueFactory<InputColumn, Boolean>("isInput"));
        Callback<TableColumn, TableCell> callBack = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell<InputColumn, Boolean> include = new TableCell<InputColumn, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        if (!empty)
                            if (item.booleanValue()) {
                                setText("include");
                            } else {
                                setText("not include");
                            }
                        super.updateItem(item, empty);
                    }
                };
                include.setOnMouseClicked(event -> {
                    TableCell tableCell = (TableCell) event.getSource();
                    TableColumn tableColumn = tableCell.getTableColumn();
                    InputColumn myModel = (InputColumn) tableCell.getTableView().getItems().get(tableCell.getTableRow().getIndex());
                    if (tableColumn.getText().equals("isInput")) {
                        if (tableCell.getText().equals("include")) {
                            tableCell.setText("not include");
                            myModel.setIsInput(false);
                        } else {
                            tableCell.setText("include");
                            myModel.setIsInput(true);
                        }
                    }
                    if (tableColumn.getText().equals("isOutput")) {
                        if (tableCell.getText().equals("include")) {
                            tableCell.setText("not include");
                            myModel.setIsOutput(false);
                        } else {
                            tableCell.setText("include");
                            myModel.setIsOutput(true);
                        }
                    }
                });
                return include;
            }
        };
        input.setCellFactory(callBack);
        output.setCellValueFactory(
                new PropertyValueFactory<InputColumn, Boolean>("isOutput"));
        output.setCellFactory(callBack);
        columnTable.getColumns().addAll(columnName, input, output);
    }

    private void setHeaders(List<String> headers) {
        for (int i = 0; i < headers.size(); i++) {
            dataTable.getColumns().addAll(getTableColumn(headers.get(i), i));
        }
    }

    private void setData(List<List<String>> dataFile) {
        ObservableList<Object> objects = FXCollections.observableArrayList();
        for (List<String> rowFile : dataFile) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (String cell : rowFile) {
                row.add(cell);
            }
            objects.add(row);
        }
        dataTable.getItems().clear();
        dataTable.setItems(objects);
    }

    public TableColumn<ObservableList<String>, String> getTableColumn(String name, int index) {
        TableColumn<ObservableList<String>, String> tableColumn = new TableColumn<>(name);
        tableColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().get(index)));
        tableColumn.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
            @Override
            public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> soCalledFriendBooleanTableColumn) {
                TableCell<ObservableList<String>, String> tableCell = new TableCell<ObservableList<String>, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
                tableCell.setOnMouseClicked(action -> {
                });
                return tableCell;
            }
        });
        return tableColumn;
    }
}
