package controller.menu_controllers;

import controller.MainController;
import controller.messages.MenuMessages;
import model.Database;
import model.User;
import view.menus.AppMenu;

public class LoginMenuController {
    private static int attemptNumber = 0;
    public static MenuMessages userLogin(String username, String password, Boolean stayLoggedIn) {
        User user = Database.getUserByName(username);
        if (user == null)
            return MenuMessages.NO_USER_WITH_USERNAME;
        else if (!user.getPassword().equals(password)) {
            if (attemptNumber >= 1) {
                AppMenu.show("you entered password incorrect " + LoginMenuController.getAttemptNumber()
                        + " times wait for " + (LoginMenuController.getAttemptNumber() * 5) + " seconds");
                pause(5000 * attemptNumber);
                attemptNumber++;
                return MenuMessages.STAY;
            }
            else {
                attemptNumber++;
                return MenuMessages.PASSWORD_INCORRECT;
            }
        }
        if (stayLoggedIn) Database.saveStayLoggedInUser(user);
        MainController.setCurrentUser(user);
        attemptNumber = 0;
        return MenuMessages.USER_LOGGED_IN_SUCCESSFULLY;
    }

    public static MenuMessages forgotPassword(String username) {
        User user = Database.getUserByName(username);
        if (user == null)
            return MenuMessages.NO_USER_WITH_USERNAME;
        else {
            String answer = AppMenu.getOneLine(user.getSecurityQuestion().getQuestion());
            if (answer.equals(user.getSecurityQuestion().getAnswer())) {
                String newPassword = AppMenu.getOneLine("enter recovery password:");
                if (!MenuController.isPasswordStrong(newPassword).equals(MenuMessages.STRONG_PASSWORD))
                    return MenuController.isPasswordStrong(newPassword);
                else {
                    user.setPassword(newPassword);
                    Database.saveAllUsers();
                    AppMenu.show("your new password: " + newPassword);
                    return MenuMessages.SECURITY_QUESTION_CONFIRMED;
                }

            } else
                return MenuMessages.INCORRECT_QNA_ANSWER;

        }

    }
    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static int getAttemptNumber() {
        return attemptNumber;
    }
    public static void userLogOut() {
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
        Database.saveStayLoggedInUser(null);
    }
}
