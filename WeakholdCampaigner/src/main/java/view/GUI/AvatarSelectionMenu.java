package view.GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;


import java.io.File;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;


public class AvatarSelectionMenu extends AbstractMenu{
    private static SupportsAvatarSelection previousMenu;

    private ImageView selectedAvatar = null; //start it with random
    private static int numOfAvatars = 9;

    @Override
    public void start(Stage primaryStage) { //should I say: throws exception ?
        throw new UnsupportedOperationException("This method is not meant to be called. Call manualStart(...) instead.");
    }

    public void manualStart(Stage stage, SupportsAvatarSelection previousMenu) throws Exception {
        AvatarSelectionMenu.previousMenu = previousMenu;

        super.start(stage);
    }

    @Override
    protected void initialize() {
        TilePane avatarsTilePane = new TilePane();
        avatarsTilePane.setAlignment(Pos.CENTER);

        Button button1 = new Button("Choose an avatar from your PC");
        button1.setOnMouseClicked(this::chooseAvatarFromPC);

        VBox vBox = SceneBuilder.getLabeledVBox("Select your avatar", avatarsTilePane, button1);
        borderPane.setCenter(vBox);


        Button button2 = getSelectAvatarButton();
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setCenter(button2);

        borderPane.setBottom(borderPane2);

        loadAvatars(avatarsTilePane);
    }

    private Button getSelectAvatarButton() {
        Button button = new Button("Select");
        button.setOnMouseClicked((mouseEvent) -> {
            int avatarIndex;
            boolean avatarIsRandom = false;
            String selectedAvatarURL;
            if (selectedAvatar == null || selectedAvatar.getImage().getUrl().endsWith("avatarRandom.jpg")) {
                selectedAvatarURL =
                        getRandomAvatarURL();
                avatarIsRandom = true;
            }
            else
                selectedAvatarURL = selectedAvatar.getImage().getUrl();

            avatarIndex = Integer.parseInt(selectedAvatarURL.substring(
                    selectedAvatarURL.length() - "0.png".length(),
                    selectedAvatarURL.length() - ".png".length())
            );

            showInformationAlertAndWait("success",
                    "Avatar " + avatarIndex + (avatarIsRandom ? " randomly" : "") +
                            " selected successfully.");
            AvatarSelectionMenu.previousMenu.getSelectedAvatarURL(selectedAvatarURL);
        });

        return button;
    }

    public static String getRandomAvatarURL() {
        URL url;

        for (int i = 0; i < 3; i++) {
            int avatarIndex = ThreadLocalRandom.current().nextInt(0, numOfAvatars) + 1;
            url = getAvatarURL(avatarIndex);
            if (url != null) return url.toExternalForm();
        }

        return null;
    }

    @Nullable
    private static URL getAvatarURL(int index) {
        if (index > 0) return AvatarSelectionMenu.class.getResource(String.format("/Avatars/%d.png", index));
        return AvatarSelectionMenu.class.getResource("/Avatars/avatarRandom.jpg");
    }

    private void loadAvatars(TilePane tilePane) {
        tilePane.setHgap(PIXEL_UNIT / 2f);
        tilePane.setVgap(PIXEL_UNIT / 2f);

        URL url;
        int avatarIndex = 1;
        while (
                (url = getAvatarURL(avatarIndex++)) != null) {
            loadSelectableImage(url, tilePane);
        }
        numOfAvatars = avatarIndex - 1;

        if ((url = getAvatarURL(-1)) != null) loadSelectableImage(url, tilePane); //show the avatarRandom.jpg
    }

    private void loadSelectableImage(URL url, TilePane tilePane) {
        Image image = new Image(url.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(PIXEL_UNIT * 2);
        imageView.setPreserveRatio(true);

        imageView.setOnMouseClicked(event -> {
            if (selectedAvatar != null) selectedAvatar.setEffect(null);

            Lighting lighting = new Lighting();
            lighting.setLight(new Light.Distant(45, 45, Color.GRAY));
            imageView.setEffect(lighting);

            selectedAvatar = imageView;
        });

        tilePane.getChildren().add(imageView);
    }

    public void chooseAvatarFromPC(MouseEvent mouseEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your avatar.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.png"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            showInformationAlertAndWait("success",
                    "Avatar selected successfully.");
            AvatarSelectionMenu.previousMenu.getSelectedAvatarURL(file.getAbsolutePath());
        }
    }
}
