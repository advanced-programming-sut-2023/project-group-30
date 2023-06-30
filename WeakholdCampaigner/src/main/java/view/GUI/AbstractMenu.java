package view.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class AbstractMenu extends Application {
    public final static int PIXEL_UNIT = 43, X_Ratio = 32, Y_Ratio = 18, SPACING = PIXEL_UNIT / 3;
    protected static Stage stage;

    protected static void showErrorAndWait(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected static void showErrorAndWait(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showInformationAlertAndWait(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        if (header != null) alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showInformationAlertAndWait(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected static boolean showConfirmationAlertAndWait(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.showAndWait();
        //todo: add the ability to show yes/no instead of ok/cancel

        return !alert.getResult().getButtonData().isCancelButton();
    }

    public static String showTextInputDialogAndWait(String content) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setContentText(content);
        textInputDialog.showAndWait();

        String result = textInputDialog.getResult();
        return (result != null) ? result : "";
    }

    protected abstract void initialize();
    protected BorderPane borderPane;
    protected static Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        borderPane = SceneBuilder.getBorderPane();
        scene = SceneBuilder.getScene(borderPane);
        initialize();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Weakhold Campaigner");
        primaryStage.show();
    }

    public static boolean isNullOrEmpty(String... arguments) {
        for (String arg :
                arguments)
            if (arg == null || arg.equals("")) return true;

        return false;
    }
}
