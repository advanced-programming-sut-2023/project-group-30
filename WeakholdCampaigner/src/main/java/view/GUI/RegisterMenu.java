package view.GUI;

import controller.menu_controllers.MenuController;
import controller.menu_controllers.SignupMenuController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterMenu extends AbstractMenu {
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AbstractMenu.stage = primaryStage;

        borderPane = SceneBuilder.getBorderPane();
        RegisterMenu.scene = SceneBuilder.getScene(borderPane);
        initialize();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Weakhold Campaigner");
        primaryStage.show();
    }

    private String username = "", password = "", slogan = "", email = "", nickname = "", passwordConfirmation = "";
    private final Label usernameValidityLabel = new Label(""), emailValidityLabel = new Label(""),
            passwordConfirmationValidity = new Label("");
    {
        usernameValidityLabel.setTextFill(Color.color(1, 0, 0));
        emailValidityLabel.setTextFill(Color.color(1, 0, 0));
        passwordConfirmationValidity.setTextFill(Color.color(1, 0, 0));
    }

    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Register Menu");

        PasswordField passwordField = new PasswordField();
        HBox passwordHBox = getPasswordHBox(passwordField);

        Button registerButton = new Button("Register");
        registerButton.setOnMouseClicked((mouseEvent) -> {
            if (isNullOrEmpty(username, password, email, nickname, passwordConfirmation)) {
                showErrorAndWait("Error",
                        "Please fill every field marked by *");
                return;
            }

            switch (SignupMenuController.createUser(username, password, passwordConfirmation, email, nickname, slogan)) {
                case USER_CREATED_SUCCESSFULLY:
                    showInformationAlertAndWait("Registered successfully!");
                    //todo
                    break;
                case INVALID_USERNAME:
                    showErrorAndWait("Error", "Please choose a valid username.");
                    break;
                case USERNAME_TAKEN:
                    usernameValidityLabel.setTextFill(Color.color(1, 0, 0));
                    usernameValidityLabel.setText("This username is taken.");
                    break;
                case TAKEN_EMAIL:
                    emailValidityLabel.setText("This email address is taken.");
                    break;
                case INVALID_EMAIL:
                    emailValidityLabel.setText("Invalid email format.");
                    break;
                //case WRONG_RANDOM_PASSWORD_REENTERED:
                case FEW_CHARACTERS:
                case N0_LOWERCASE_LETTER:
                case N0_UPPERCASE_LETTER:
                case N0_NUMBER:
                case NO_NON_WORD_NUMBER_CHARACTER:
                    showErrorAndWait("Error", "Please choose a valid password.");
                    break;
                case WRONG_PASSWORD_CONFIRMATION:
                    passwordConfirmationValidity.setText("Confirmation does not match the password.");
                    break;

                case WRONG_SECURITY_QUESTION_FORMAT:
                    System.out.println("Error: This command should have the following format:\n" +
                            "question pick -q <question-number> -a <answer> -c <answer confirmation>");
                    break;
                case OUT_OF_BOUNDS:
                    System.out.println("Error: Your chosen question number should be between 1 and 3");
                    break;
                case WRONG_ANSWER_CONFIRM:
                    System.out.println("Error: Your answer confirmation is wrong!");
                    break;
            }
        });

        vBox.getChildren().addAll(getUsernameHBox(),
                passwordHBox,
                getPasswordConfirmationTextField(),
                getRandomPasswordButton(passwordField),
                getSloganVBox(),
                registerButton,
                getEmailTextField(),
                getNicknameTextField());

        borderPane.setCenter(vBox);
    }

    private HBox getUsernameHBox() {
        TextField usernameField = new TextField("username");

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!MenuController.isUsernameValid(newValue)) {
                usernameValidityLabel.setTextFill(Color.color(1, 0, 0));
                usernameValidityLabel.setText("Username can only contain letters, digits and underscores.");
            }
            else {
                usernameValidityLabel.setTextFill(Color.color(0, 1, 0));
                usernameValidityLabel.setText("Valid Username.");
            }

            username = newValue;
        });

        return SceneBuilder.getHBox(new Label("*Username: "), usernameField, usernameValidityLabel);
    }

    private HBox getPasswordHBox(PasswordField passwordField) {

        //passwordField.setPrefHeight(50);
        TextField textField = new TextField();
        //textField.setPrefHeight(50);
        passwordField.textProperty().bindBidirectional(textField.textProperty());

        Label passwordValidityLabel = new Label("");
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            switch (MenuController.isPasswordStrong(newValue)) {
                case FEW_CHARACTERS:
                    passwordValidityLabel.setText("Password must contain at least 6 characters.");
                    passwordValidityLabel.setTextFill(Color.color(1, 0, 0));
                    break;
                case N0_LOWERCASE_LETTER:
                    passwordValidityLabel.setText("Password must contain a lowercase letter.");
                    passwordValidityLabel.setTextFill(Color.color(1, 0, 0));
                    break;
                case N0_UPPERCASE_LETTER:
                    passwordValidityLabel.setText("Password must contain an uppercase letter.");
                    passwordValidityLabel.setTextFill(Color.color(1, 0, 0));
                    break;
                case N0_NUMBER:
                    passwordValidityLabel.setText("Password must contain a digit.");
                    passwordValidityLabel.setTextFill(Color.color(1, 0, 0));
                    break;
                case NO_NON_WORD_NUMBER_CHARACTER:
                    passwordValidityLabel.setText("Password must contain a special character.");
                    passwordValidityLabel.setTextFill(Color.color(1, 0, 0));
                    break;
                default:
                    passwordValidityLabel.setText("Valid password.");
                    passwordValidityLabel.setTextFill(Color.color(0, 1, 0));
                    break;
            }

            password = newValue;
        });

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

        return SceneBuilder.getHBox(new Label("*Password: "), stackPane, passwordValidityLabel, checkBox);
    }

    private Button getRandomPasswordButton(PasswordField passwordField) {
        Button generateRandomPasswordButton = new Button("generate random password");
        generateRandomPasswordButton.setOnMouseClicked((mouseEvent) -> {
            String randomPassword = SignupMenuController.generateRandomPassword();
            if(showConfirmationAlertAndWait(
                    "Do you want the randomly generated password below?",
                    randomPassword)
            ) {
                passwordField.setText(randomPassword);
            }
        });

        return generateRandomPasswordButton;
    }

    private VBox getSloganVBox() {
        TextField sloganTextField = new TextField();
        sloganTextField.setText("slogan");
        sloganTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            slogan = newValue;
        });

        Button generateRandomSlogan = new Button("generate random slogan");
        generateRandomSlogan.setOnMouseClicked((mouseEvent) -> {
            sloganTextField.setText(SignupMenuController.getRandomSlogan());
        });

        HBox hBox = SceneBuilder.getHBox(new Label("Slogan: "), sloganTextField, generateRandomSlogan);

        HBox commonSlogans = getCommonSlogansComboBox(sloganTextField);

        VBox sloganGroup = SceneBuilder.getVBox(hBox, commonSlogans);

        CheckBox checkBox = new CheckBox("Have a slogan");
        sloganGroup.setDisable(true);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            sloganGroup.setDisable(!newValue);
            if (!newValue) slogan = "";
        });

        return SceneBuilder.getVBox(checkBox, sloganGroup);
    }

    private HBox getCommonSlogansComboBox(TextField sloganTextField) {
        Label label = new Label("Choose from popular slogans: ");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Choose from popular slogans");
        comboBox.getItems().addAll(SignupMenuController.getAllSlogans());


        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            sloganTextField.setText(newValue);
        });

        return SceneBuilder.getHBox(label, comboBox);
    }

    private HBox getEmailTextField() {
        TextField emailTextField = new TextField("me@mail.com");
        emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            email = newValue;
        });

        return SceneBuilder.getHBox(new Label("*Email: "), emailTextField, emailValidityLabel);
    }

    private HBox getNicknameTextField() {
        TextField nicknameTextField = new TextField("Nickname");
        nicknameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            nickname = newValue;
        });

        return SceneBuilder.getHBox(new Label("*Nickname: "), nicknameTextField);
    }

    private HBox getPasswordConfirmationTextField() {
        PasswordField passwordConfirmationTextField = new PasswordField();
        passwordConfirmationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordConfirmation = newValue;
        });

        return SceneBuilder.getHBox(new Label("*Confirm your password: "), passwordConfirmationTextField,
                passwordConfirmationValidity);
    }
}
