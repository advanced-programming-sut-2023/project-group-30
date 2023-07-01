package view.GUI;

import controller.menu_controllers.MenuController;
import controller.menu_controllers.ProfileMenuController;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public class ProfileMenu extends AbstractMenu implements SupportsAvatarSelection {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initialize() {
        captchaValidity = new TextField();

        VBox vBox = SceneBuilder.getLabeledVBox("Profile Menu",
                SceneBuilder.getHBox(getUserInfo(), getUserSlogan(), getAvatarRectangle()),
                getChangeAvatarButton(), getChangeUsernameVBox() , getChangePasswordVBox(),
                getChangeNicknameVBox(), getChangeEmailVBox(), getChangeSloganVBox(), getMainMenuButton());
        borderPane.setCenter(vBox);
    }

    private static HBox getUserInfo() {
        Label usernameLabel = new Label("Username: " + ProfileMenuController.getUsername());
        //we probably shouldn't display the password
        Label nicknameLabel = new Label("Nickname: " + ProfileMenuController.getNickname());
        Label emailLabel = new Label("Email: " + ProfileMenuController.getEmail());
        return SceneBuilder.getHBox(usernameLabel, nicknameLabel, emailLabel);
    }

    private static HBox getUserSlogan() {
        return SceneBuilder.getHBox(new Label("Slogan: " + ProfileMenuController.getSlogan()));
    }

    @Override
    public void getSelectedAvatarURL(String selectedAvatarURL) {
        //stage.setScene(scene);
        ProfileMenuController.setAvatarURL(selectedAvatarURL);
        //todo
        try {
            start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Rectangle getAvatarRectangle() {
        Rectangle avatar = new Rectangle(1.5 * PIXEL_UNIT, 1.5 * PIXEL_UNIT);
        try {
            Image image = new Image(ProfileMenuController.getAvatarURL());
            ImagePattern imagePattern = new ImagePattern(image);
            avatar.setFill(imagePattern);
        } catch (Exception exception) {
            avatar.setFill(new Color(0.69, 0.69, 0.69, 1));
        }

        return avatar;
    }

    private Button getChangeAvatarButton() {
        Button button = new Button("Change avatar");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new AvatarSelectionMenu().manualStart(stage, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    private Button getMainMenuButton() {
        Button button = new Button("Main Menu");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new MainMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    private VBox getChangeUsernameVBox() {
        Label usernameValidityLabel = new Label("");
        usernameValidityLabel.setTextFill(Color.color(1, 0, 0));

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
        });


        Button button = new Button("Change your username");
        button.setOnMouseClicked((mouseEvent) -> {
            String username = usernameField.getText();
            if (username == null) {
                showErrorAndWait("Please enter a new username.");
                return;
            }

            switch (ProfileMenuController.changeUsername(username)) {
                case USERNAME_HAS_CHANGED:
                    showInformationAlertAndWait("Username changed successfully.");
                    initialize();
                    break;
                case INVALID_USERNAME:
                    showErrorAndWait("Please enter a valid username.");
                    break;
                case TAKEN_USERNAME:
                    showErrorAndWait("This username is taken");
                    break;
            }
        });

        return SceneBuilder.getVBox(SceneBuilder.getHBox(
                new Label("New username: "), usernameField, usernameValidityLabel),
                button);
    }

    private VBox getChangePasswordVBox() {
        PasswordField oldPasswordField = new PasswordField();
        TextField oldTextField = new TextField();
        oldPasswordField.textProperty().bindBidirectional(oldTextField.textProperty());

        StackPane oldStackPane = new StackPane(oldTextField, oldPasswordField);
        CheckBox oldCheckBox = new CheckBox("Show old password");
        oldCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                oldTextField.toFront();
            }
            else {
                oldPasswordField.toFront();
            }
        });


        PasswordField passwordField = new PasswordField();
        TextField textField = new TextField();
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


        PasswordField passwordConfirmationField = new PasswordField();

        Button changePasswordButton = new Button("Change your password");
        changePasswordButton.setOnMouseClicked((mouseEvent) -> {
            String newPassword = passwordField.getText(),
                    newPasswordConfirmation = passwordConfirmationField.getText(),
                    oldPassword = oldPasswordField.getText();

            if (isNullOrEmpty(newPassword, newPasswordConfirmation, oldPassword)) {
                showErrorAndWait("Please fill all three password fields.");
                return;
            }

            if (!newPassword.equals(newPasswordConfirmation)) {
                showErrorAndWait("Password confirmation does not match the password.");
                return;
            }

            showCaptcha();
            captchaValidity.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("Y")) {
                    switch (ProfileMenuController.changePassword(oldPassword, newPassword)) {
                        case INCORRECT_CURRENT_PASSWORD:
                            showErrorAndWait("Your old password is wrong!");
                            break;
                        case SAME_PASSWORD:
                            showErrorAndWait("Your new password is the same as the previous one");
                            break;
                        case PASSWORD_HAS_CHANGED:
                            showInformationAlertAndWait("Password changed successfully.");
                            break;
                        case FEW_CHARACTERS:
                            showErrorAndWait("Your password should have at least 6 character");
                            break;
                        case N0_LOWERCASE_LETTER:
                            showErrorAndWait("Your password doesn't have any lowercase letter");
                            break;
                        case N0_UPPERCASE_LETTER:
                            showErrorAndWait("Your password doesn't have any uppercase letter");
                            break;
                        case N0_NUMBER:
                            showErrorAndWait("Your password doesn't have any number");
                            break;
                        case NO_NON_WORD_NUMBER_CHARACTER:
                            showErrorAndWait("Your password doesn't have any special characters");
                            break;
                    }

                    stage.setScene(scene);

                    captchaValidity = new TextField();
                }
                else {
                    stage.setScene(scene);
                    captchaValidity = new TextField();
                }
            });
        });

        return SceneBuilder.getVBox(
                SceneBuilder.getHBox(new Label("Current password: "), oldStackPane, oldCheckBox),
                SceneBuilder.getHBox(new Label("New password: "), stackPane, passwordValidityLabel, checkBox),
                SceneBuilder.getHBox(new Label("Confirm your new password: "), passwordConfirmationField),
                changePasswordButton
        );
    }

    private VBox getChangeNicknameVBox() {
        TextField nicknameTextField = new TextField("New nickname");

        Button button = new Button("Change nickname");
        button.setOnMouseClicked((mouseEvent) -> {
            String nickname = nicknameTextField.getText();
            if (isNullOrEmpty(nickname)) {
                showErrorAndWait("Please enter your new nickname.");
                return;
            }

            ProfileMenuController.changeNickname(nickname);
            showInformationAlertAndWait("Nickname changed successfully");
            initialize();
        });

        return SceneBuilder.getVBox(
                SceneBuilder.getHBox(new Label("New nickname: "), nicknameTextField),
                button
        );
    }

    private VBox getChangeEmailVBox() {
        TextField emailTextField = new TextField("New email");

        Button button = new Button("Change email");
        button.setOnMouseClicked((mouseEvent) -> {
            String email = emailTextField.getText();
            if (isNullOrEmpty(email)) {
                showErrorAndWait("Please enter your new email address.");
                return;
            }

            switch (ProfileMenuController.changeEmail(email)) {
                case EMAIL_HAS_CHANGED:
                    showInformationAlertAndWait("Email changed successfully.");
                    initialize();
                    break;
                case INVALID_EMAIL:
                    showErrorAndWait("Your email format is invalid.");
                    break;
                case TAKEN_EMAIL:
                    showErrorAndWait("This email address is taken.");
                    break;
            }
        });

        return SceneBuilder.getVBox(
                SceneBuilder.getHBox(new Label("New email: "), emailTextField),
                button
        );
    }

    private VBox getChangeSloganVBox() {
        TextField sloganTextField = new TextField("New slogan");

        Button changeSlogan = new Button("Change slogan");
        changeSlogan.setOnMouseClicked((mouseEvent) -> {
            String slogan = sloganTextField.getText();
            if (isNullOrEmpty(slogan)) {
                showErrorAndWait("Please enter your new slogan.");
                return;
            }

            ProfileMenuController.changeSlogan(slogan);
            showInformationAlertAndWait("Slogan changed successfully.");
            initialize();
        });

        Button removeSlogan = new Button("Remove slogan");
        removeSlogan.setOnMouseClicked((mouseEvent) -> {
            ProfileMenuController.changeSlogan(null);
            showInformationAlertAndWait("Slogan removed successfully.");
            initialize();
        });

        return SceneBuilder.getVBox(
                SceneBuilder.getHBox(new Label("New slogan: "), sloganTextField),
                changeSlogan,
                removeSlogan
        );
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
