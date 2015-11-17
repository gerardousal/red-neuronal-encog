package nd.nn.stdper.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nd.nn.stdper.impl.domain.TabularData;
import nd.nn.stdper.impl.service.LoadFileService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by driucorado on 11/13/15.
        */
public class MainController implements Initializable {
    @FXML
    private Button btnLoad;
    @FXML
    private TextField txtDelimiter;

    private Stage stage;
    private LoadFileService loadFileService;
    private FileChooser fileChooser;
    private DataContentController  dataContentController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFileService = new LoadFileService();
        txtDelimiter.setText(","); //default value
        fileChooser = new FileChooser();
        AnchorPane dataPane = getDataContentView();
        Stage dataStage = new Stage();
        dataStage.setScene(new Scene(dataPane));
        btnLoad.setOnMouseClicked(event -> {
            loadFileService.reset();
            loadFileService
                    .setFile(fileChooser.showOpenDialog(this.stage))
                    .setDelimiter(txtDelimiter.getText());
            loadFileService.start();
        });
        loadFileService.setOnSucceeded(event -> {
            TabularData tabularData = (TabularData) event.getSource().getValue();
            dataContentController.setTabularData(tabularData);
            dataStage.show();
        });
    }

    private AnchorPane getDataContentView() {
        try {
            FXMLLoader parent = new FXMLLoader(getClass().getResource("/fxml/dataContent.fxml"));
            AnchorPane anchorPane = (AnchorPane) parent.load();
            dataContentController = (DataContentController) parent.getController();
            return anchorPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
