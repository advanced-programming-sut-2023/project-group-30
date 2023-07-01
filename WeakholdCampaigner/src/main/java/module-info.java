module WeakholdCampaigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires com.google.gson;
    requires java.desktop; //java.desktop is for captcha generation



    opens view to javafx.fxml;
    //opens view.GUI to javafx.fxml;

    opens model to javafx.fxml, javafx.controls, com.google.gson;

    opens model.game.game_entities to com.google.gson;

    opens model.game.map to com.google.gson;
    opens model.enums to javafx.controls,javafx.graphics;
    exports model.enums to com.google.gson;
    exports model.game to com.google.gson;




    opens model.game to com.google.gson;

    exports model.game.game_entities to javafx.graphics;
    exports controller.menu_controllers to javafx.graphics;
    exports model.game.map to com.google.gson;


    exports controller;
    exports model;
    exports view;
    exports view.GUI;


}