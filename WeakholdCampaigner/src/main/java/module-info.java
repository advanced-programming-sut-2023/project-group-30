module WeakholdCampaigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires com.google.gson;
    requires java.desktop; //java.desktop is for captcha generation



    opens network.client.GUI to javafx.fxml;

    exports network.server.controller.menu_controllers to javafx.graphics;


    exports network.server.controller;
    exports network.client.GUI;
    exports network.server;
    opens network.server to com.google.gson, javafx.controls, javafx.fxml;
    exports network.server.model;
    opens network.server.model to com.google.gson, javafx.controls, javafx.fxml;
    opens network.common to com.google.gson;

    exports network.common.messages;
    exports network.common;
}