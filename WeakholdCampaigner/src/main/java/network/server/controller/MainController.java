package server.controller;

import server.Database;
import server.model.User;

import java.util.Scanner;

public class MainController {
    private static User currentUser; //todo remove this
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
