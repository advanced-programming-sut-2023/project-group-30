package view.GUI;

import controller.menu_controllers.LoginMenuController;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.concurrent.ThreadLocalRandom;

//todo should get captcha before checking if the password is right.
// in the forgot password option, the password should not be visible
// smelly captcha
public class LoginMenu extends AbstractMenu{
    public static void main(String[] args) {
        launch(args);
    }

    private static PasswordField passwordField;
    private static TextField usernameField;
    private static CheckBox stayLoggedInCheckBox;
    @Override
    protected void initialize() {
        captchaValidity = new TextField();

        VBox vBox = SceneBuilder.getLabeledVBox("Login Menu", getUsernameHBox(), getPasswordHBox(),
                getStayLoggedInBox(), getForgotPasswordButton(), getLoginButton(), getRegisterMenuButton());
        borderPane.setCenter(vBox);
    }

    private HBox getUsernameHBox() {
        usernameField = new TextField("Username");
        return SceneBuilder.getHBox(new Label("Username: "), usernameField);
    }

    private HBox getPasswordHBox() {
        passwordField = new PasswordField();

        TextField textField = new TextField();
        passwordField.textProperty().bindBidirectional(textField.textProperty());

        StackPane stackPane = new StackPane(textField, passwordField);
        CheckBox checkBox = new CheckBox("Show password");
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.toFront();
            }
            else {
                passwordField.toFront();
            }
        });

        return SceneBuilder.getHBox(new Label("Password: "), stackPane, checkBox);
    }

    private Button getForgotPasswordButton() {
        Button button = new Button("Forgot my password");
        button.setOnMouseClicked((mouseEvent) -> {
            String username = usernameField.getText();
            if (isNullOrEmpty(username)) {
                showErrorAndWait("Please enter your username.");
                return;
            }

            switch (LoginMenuController.forgotPassword(username)) {
                case USERNAME_DOES_NOT_EXIST:
                    showErrorAndWait("This username does not exist.");
                    break;
                case INCORRECT_QNA_ANSWER:
                    showErrorAndWait("Wrong answer.");
                    break;
                case FEW_CHARACTERS:
                    showErrorAndWait("Your password should have at least 6 characters.");
                    break;
                case N0_LOWERCASE_LETTER:
                    showErrorAndWait("Your password should contain a lowercase letter.");
                    break;
                case N0_UPPERCASE_LETTER:
                    showErrorAndWait("Your password should contain an uppercase letter.");
                    break;
                case N0_NUMBER:
                    showErrorAndWait("Your password should contain a digit.");
                    break;
                case NO_NON_WORD_NUMBER_CHARACTER:
                    showErrorAndWait("Your password should contain a special character.");
                    break;
                case SECURITY_QUESTION_CONFIRMED:
                    showInformationAlertAndWait("Password changed successfully.");
                    break;
                default:
                    showErrorAndWait("Something went wrong");
                    break;
            }
        });

        return button;
    }

    private CheckBox getStayLoggedInBox() {
        stayLoggedInCheckBox = new CheckBox("Stay logged in?");

        return stayLoggedInCheckBox;
    }

    private Button getLoginButton() {
        Button button = new Button("Login");
        button.setOnMouseClicked((mouseEvent) -> {
            String username = usernameField.getText() , password = passwordField.getText();

            if (isNullOrEmpty(username, password)) {
                showErrorAndWait("Please enter your credentials.");
                return;
            }

            switch (LoginMenuController.userLogin(username, password, stayLoggedInCheckBox.isSelected())) {
                case USERNAME_DOES_NOT_EXIST:
                    showErrorAndWait("This username does not exist");
                    break;
                case PASSWORD_INCORRECT:
                    showErrorAndWait("Password is incorrect");
                    break;
                case STAY:
                    //Thread.sleep(ms);
                    break;
                case USER_LOGGED_IN_SUCCESSFULLY:
                    showCaptcha();
                    captchaValidity.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.equals("Y")) {
                            showInformationAlertAndWait("Logged in successfully!");

                            //goto MainMenu:
                            //MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
                            try {
                                new MainMenu().start(stage);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            stage.setScene(scene);

                            captchaValidity = new TextField();
                        }
                    });

                    break;
            }
        });

        return button;
    }

    private static Button getRegisterMenuButton() {
        Button button = new Button("Register Menu");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new RegisterMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    //Captcha:
    private static TextField captchaValidity;

    public void showCaptcha() {
        String randomCaptchaNumber = Integer.toString(ThreadLocalRandom.current().nextInt(111111, 999999));

        ImageView imageView = new ImageView(
                Captcha.getCaptcha(randomCaptchaNumber));

        TextField textField = new TextField("Enter the Captcha");

        Button newCaptchaButton = new Button("New Captcha");
        newCaptchaButton.setOnMouseClicked((mouseEvent) -> {
            //smelly
            showCaptcha();
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnMouseClicked((mouseEvent) -> {
            if (randomCaptchaNumber.equals(textField.getText())) {
                captchaValidity.setText("Y");
                return;
            }

            showErrorAndWait("Wrong");
        });

        VBox vBox = SceneBuilder.getLabeledVBox("Captcha");
        vBox.getChildren().addAll(imageView, textField, newCaptchaButton, submitButton);

        BorderPane captchaPane = SceneBuilder.getBorderPane();
        captchaPane.setCenter(vBox);
        stage.setScene(SceneBuilder.getScene(captchaPane));
    }
}
