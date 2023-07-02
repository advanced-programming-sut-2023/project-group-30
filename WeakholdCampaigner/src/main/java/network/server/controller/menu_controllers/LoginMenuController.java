package network.server.controller.menu_controllers;

import network.server.controller.MainController;
import network.common.messages.MenuMessages;
import network.server.Database;
import network.server.model.User;

public class LoginMenuController extends MenuController {
    private static int attemptNumber = 0;

    public static MenuMessages userLogin(String username, String password, Boolean stayLoggedIn) {
        User user = Database.getUserByName(username);
        if (user == null)
            return MenuMessages.USERNAME_DOES_NOT_EXIST;
        else if (!user.getPassword().equals(sha256(password))) {
            if (attemptNumber >= 1) {
                //todo lock the user to prevent it from using brute force
            } else {
                attemptNumber++;
                return MenuMessages.PASSWORD_INCORRECT;
            }
        }
        if (stayLoggedIn) Database.saveStayLoggedInUser(user);
        MainController.setCurrentUser(user);
        attemptNumber = 0;
        return MenuMessages.USER_LOGGED_IN_SUCCESSFULLY;
    }

    /*todo
    public static MenuMessages forgotPassword(String username) {
        User user = Database.getUserByName(username);
        if (user == null)
            return MenuMessages.USERNAME_DOES_NOT_EXIST; //is this return value handled in the caller?
        else {
            String answer = AppMenu.getOneLine(user.getSecurityQuestion().getQuestion());
            if (answer.equals(user.getSecurityQuestion().getAnswer())) {
                String newPassword = AppMenu.getOneLine("enter recovery password:");
                if (!MenuController.isPasswordStrong(newPassword).equals(MenuMessages.STRONG_PASSWORD))
                    return MenuController.isPasswordStrong(newPassword);
                else {
                    user.setPassword(sha256(newPassword));
                    Database.saveAllUsers();
                    AppMenu.show("your new password: " + newPassword);
                    return MenuMessages.SECURITY_QUESTION_CONFIRMED;
                }

            } else
                return MenuMessages.INCORRECT_QNA_ANSWER;

        }

    }*/

    public static void userLogOut() {
        Database.saveStayLoggedInUser(null);
    }
}
