package view.GUI;

import controller.menu_controllers.MenuController;
import controller.menu_controllers.SignupMenuController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterMenu extends AbstractMenu {
    private static Scene scene;
    private static final SignupMenuController signupMenuController = new SignupMenuController();

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
        //smelly:
        pickedAnswer = new TextField();
        captchaValidity = new TextField();

        VBox vBox = SceneBuilder.getLabeledVBox("Register Menu");

        PasswordField passwordField = new PasswordField();
        HBox passwordHBox = getPasswordHBox(passwordField);

        Button registerButton = getRegisterButton();

        vBox.getChildren().addAll(getUsernameHBox(),
                passwordHBox,
                getPasswordConfirmationTextField(),
                getRandomPasswordButton(passwordField),
                getSloganVBox(),
                getEmailTextField(),
                getNicknameTextField(),
                registerButton);

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

    private static TextField pickedAnswer;
    private static Integer pickedQuestion = null;
    private void showSecurityQuestion() {
        List<String> questions = Arrays.asList("1. What is my father’s name?", "2. What was my first pet’s name?",
                "3. What is my mother’s last name?");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Pick a question");
        comboBox.getItems().addAll(questions);

        TextField answerField = new TextField("Answer");

        Button submitAnswerButton = new Button("Submit your answer");
        submitAnswerButton.setOnMouseClicked((mouseEvent) -> {
            String question = comboBox.getValue(), answer = answerField.getText();

            if (question == null) {
                showErrorAndWait("Please pick a question.");
                return;
            }
            if (answer == null) {
                showErrorAndWait("Please write an answer.");
                return;
            }

            pickedQuestion = questions.indexOf(question);
            pickedAnswer.setText(answer);
        });

        VBox vBox = SceneBuilder.getLabeledVBox("Pick a security question");
        vBox.getChildren().addAll(comboBox, answerField, submitAnswerButton);

        BorderPane securityQuestionPane = SceneBuilder.getBorderPane();
        securityQuestionPane.setCenter(vBox);
        stage.setScene(SceneBuilder.getScene(securityQuestionPane));
    }

    private Button getRegisterButton() {
        Button registerButton = new Button("Register");
        registerButton.setOnMouseClicked((mouseEvent) -> {
            if (isNullOrEmpty(username, password, email, nickname, passwordConfirmation)) {
                showErrorAndWait("Error",
                        "Please fill every field marked by *");
                return;
            }

            switch (SignupMenuController.validateUserCreation(username, password, passwordConfirmation,
                    email)) {
                case SUCCESS:
                    showInformationAlertAndWait("You will be registered shortly",
                            "Just pick a security question and fill a captcha and you're ready to go!");

                    showSecurityQuestion();
                    pickedAnswer.textProperty().addListener((observable, oldValue, newAnswerValue) -> {
                        showCaptcha();
                        captchaValidity.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                            if (newValue1.equals("Y")) {
                                SignupMenuController.newCreateUser(username, password, email, nickname,
                                        slogan, pickedQuestion, newAnswerValue);
                                showInformationAlertAndWait("Registered successfully!");

                                //todo enter another menu
                                stage.setScene(scene);

                                pickedQuestion = null;
                                pickedAnswer = new TextField();
                                captchaValidity = new TextField();
                            }
                        });

                    });
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
                default:
                    showErrorAndWait("something went wrong");
                    break;
            }
        });

        return registerButton;
    }

    //Captcha:
    private static TextField captchaValidity;
    protected static int randomNum(int num) {
        return (new Random()).nextInt(num);
    }
    private static java.awt.Color randomColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + randomNum(bc - fc);
        int g = fc + randomNum(bc - fc);
        int b = fc + randomNum(bc - fc);
        return new java.awt.Color(r, g, b);
    }
    public static BufferedImage getCaptcha(String captchaKey, int width, int height) {
        Font font = new Font("Verdana", Font.ITALIC
                | Font.BOLD, 28);

        char[] chars = captchaKey.toCharArray();

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D bufferedImageGraphics = (Graphics2D) bufferedImage.getGraphics();
        AlphaComposite ac3;
        java.awt.Color color;
        int len = chars.length;
        bufferedImageGraphics.setColor(java.awt.Color.WHITE);
        bufferedImageGraphics.fillRect(0, 0, width, height);

        //key generation:
        bufferedImageGraphics.setFont(font);
        int h = height - ((height - font.getSize()) >> 1), w = width
                / len, size = w - font.getSize() + 1;
        for (int i = 0; i < len; i++) {
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    0.7f);
            bufferedImageGraphics.setComposite(ac3);

            color = new java.awt.Color(20 + randomNum(110), 30 + randomNum(110),
                    30 + randomNum(110));
            bufferedImageGraphics.setColor(color);
            bufferedImageGraphics.drawString(chars[i] + "", (width - (len - i) * w) + size,
                    h - 4);
        }

        //Noise generation:
        for (int i = 0; i < (width * height / 100); i++) {
            color = randomColor(150, 250);
            bufferedImageGraphics.setColor(color);
            bufferedImageGraphics.drawOval(randomNum(width), randomNum(height), 5 + randomNum(width/30),
                    5 + randomNum(height/30));
        }
        for (int i = 0; i < (width * height / 700); i++) {
            color = randomColor(150, 250);
            bufferedImageGraphics.setColor(color);

            bufferedImageGraphics.drawLine(randomNum(width), randomNum(height), randomNum(width), randomNum(height));
        }


        return bufferedImage;
    }

    private Image bufferedImageToImage(BufferedImage img){
        //javafx.scene.image.Image

        //converting to a good type, read about types here:
        // https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

        //converting the IntBuffer to an Image, read more about it here:
        // https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(newImg.getWidth(), newImg.getHeight(),
                buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }


    public void showCaptcha() {
        String randomCaptchaNumber = Integer.toString(ThreadLocalRandom.current().nextInt(111111, 999999));

        ImageView imageView = new ImageView(
                bufferedImageToImage(
                        getCaptcha(randomCaptchaNumber, PIXEL_UNIT * 5, PIXEL_UNIT * 5)));

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
