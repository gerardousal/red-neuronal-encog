package nd.nn.stdper.view;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nd.nn.stdper.impl.domain.TabularData;
import nd.nn.stdper.impl.service.LoadFileService;
import nd.nn.stdper.impl.service.TabularDataFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by driucorado on 11/8/15.
 */
public class MainWindow {
    DataWindow dataWindow;

    public MainWindow init(Stage primaryStage) throws IOException {
        FXMLLoader parent =  new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane pane = parent.load();
        primaryStage.setScene(new Scene(pane));
        MainController mainController = (MainController) parent.getController();
        mainController.setStage(primaryStage);
        this.dataWindow = new DataWindow().init(new Stage());
        primaryStage.show();
        return this;
    }



}
