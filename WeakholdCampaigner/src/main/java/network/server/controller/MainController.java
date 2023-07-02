package network.server.controller;

import network.server.Database;
import network.server.model.User;

import java.util.Scanner;

public class MainController {
    private static User currentUser; //todo: is this fine?
    private static final Scanner scanner = new Scanner(System.in);

    public void initializeApp() {
        currentUser = null;

        Database.loadData();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        MainController.currentUser = currentUser;
    }
}
