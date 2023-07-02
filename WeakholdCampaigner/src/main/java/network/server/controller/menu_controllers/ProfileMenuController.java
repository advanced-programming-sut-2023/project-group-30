package server.controller.menu_controllers;

import common.messages.MenuMessages;
import server.controller.MainController;
import server.Database;
import server.model.User;
import org.jetbrains.annotations.Nullable;
import server.view.AppMenu;

import java.util.ArrayList;


public class ProfileMenuController extends MenuController {
    public static MenuMessages changeUsername(String username) {
        if (!isUsernameValid(username)) return MenuMessages.INVALID_USERNAME;
        else if (Database.getUserByName(username) != null) {
            return MenuMessages.TAKEN_USERNAME;
        }
        MainController.getCurrentUser().setUsername(username);
        Database.saveAllUsers();
        return MenuMessages.USERNAME_HAS_CHANGED;
    }

    public static MenuMessages changeNickname(String nickname) {
        MainController.getCurrentUser().setNickname(nickname);
        Database.saveAllUsers();
        return MenuMessages.NICKNAME_HAS_CHANGED;
    }

    public static MenuMessages changePassword(String oldPassword, String newPassword) {
        if (!MainController.getCurrentUser().getPassword().equals(sha256(oldPassword))) {
            return MenuMessages.INCORRECT_CURRENT_PASSWORD;
        } else if (oldPassword.equals(newPassword)) {
            return MenuMessages.SAME_PASSWORD;
        } else if (!isPasswordStrong(newPassword).equals(MenuMessages.STRONG_PASSWORD)) {
            return isPasswordStrong(newPassword);
        }
        //if (!AppMenu.getOneLine("Please enter your new password again: ").equals(newPassword))
        //    return MenuMessages.WRONG_PASSWORD_CONFIRMATION;
        MainController.getCurrentUser().setPassword(sha256(newPassword));
        Database.saveAllUsers();
        return MenuMessages.PASSWORD_HAS_CHANGED;
    }

    public static MenuMessages changeEmail(String email) {
        if (Database.getUserByEmail(email.toLowerCase()) != null)
            return MenuMessages.TAKEN_EMAIL;
        else if (!isEmailValid(email))
            return MenuMessages.INVALID_EMAIL;
        MainController.getCurrentUser().setEmail(email.toLowerCase());
        Database.saveAllUsers();
        return MenuMessages.EMAIL_HAS_CHANGED;
    }

    public static MenuMessages changeSlogan(@Nullable String slogan) {
        MainController.getCurrentUser().setSlogan(slogan);
        Database.saveAllUsers();
        return MenuMessages.SLOGAN_HAS_CHANGED;
    }

    public static Integer displayHighScore() {
        Integer highscore = MainController.getCurrentUser().getHighScore();
        if (highscore == null) return 0;
        return highscore;
    }

    private static Integer getRank(ArrayList<User> sortedUserByRank) {
        return sortedUserByRank.indexOf(MainController.getCurrentUser());
    }

    public static Integer displayRank() {
        return getRank(Database.sortUserByRank());
    }

    public static MenuMessages displaySlogan() {
        if (MainController.getCurrentUser().getSlogan() == null) {
            return MenuMessages.NULL_SLOGAN;
        }
        return MenuMessages.DISPLAY;
    }

    public static void displayProfile() {
        AppMenu.show("Username: " + MainController.getCurrentUser().getUsername());
        AppMenu.show("Email: " + MainController.getCurrentUser().getEmail());
        AppMenu.show("Nickname: " + MainController.getCurrentUser().getNickname());
        if (MainController.getCurrentUser().getSlogan() != null) {
            AppMenu.show("Slogan: " + MainController.getCurrentUser().getSlogan());
        }
    }

    public static String getUsername() {
        return MainController.getCurrentUser().getUsername();
    }

    public static String getNickname() {
        return MainController.getCurrentUser().getNickname();
    }

    public static String getEmail() {
        return MainController.getCurrentUser().getEmail();
    }

    public static String getAvatarURL() {
        return MainController.getCurrentUser().getAvatarURL();
    }

    public static void setAvatarURL(String selectedAvatarURL) {
        MainController.getCurrentUser().setAvatarURL(selectedAvatarURL);
        //smelly:
        Database.saveAllUsers();
    }

    public static String getSlogan() {
        String slogan = MainController.getCurrentUser().getSlogan();
        return (slogan != null) ? slogan : "slogan is empty";
    }
}
