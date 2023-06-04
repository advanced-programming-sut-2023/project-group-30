module WeakholdCampaigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires com.google.gson;



    opens view to javafx.fxml;
    //opens view.GUI to javafx.fxml;

    opens model to javafx.fxml, javafx.controls, com.google.gson;

    opens model.game.game_entities to com.google.gson;
    opens model.game.map to com.google.gson;
    opens model.enums to javafx.controls,javafx.graphics;
    exports model.enums to com.google.gson;
    exports model.game to com.google.gson;

    exports model;
    exports view;
    exports view.GUI;

    exports controller;
}