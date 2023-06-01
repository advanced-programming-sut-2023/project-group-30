module WeakholdCampaigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires com.google.gson;




    exports view;
    opens model to  javafx.fxml, javafx.controls;
    opens view to javafx.fxml;
    opens model.game.game_entities to com.google.gson;
    opens model.game.map to com.google.gson;
    opens model.enums to javafx.controls,javafx.graphics;

    exports controller.menu_controllers to javafx.graphics;
    exports model.enums to com.google.gson;
    exports model.game to com.google.gson;
    exports controller;
    exports model;


}