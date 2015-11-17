package nd.nn.stdper;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.util.csv.CSVFormat;

import java.io.File;

/**
 * Created by driucorado on 11/9/15.
 */
public class MidNNExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);

        EncogAnalyst analyst = new EncogAnalyst();
        AnalystWizard wizard = new AnalystWizard(analyst);
        wizard.wizard(file, true, AnalystFileFormat.DECPNT_COMMA);

        AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
        norm.analyze(file , true, CSVFormat.ENGLISH, analyst);
        norm.normalize(fileChooser.showSaveDialog(primaryStage));


    }
}
