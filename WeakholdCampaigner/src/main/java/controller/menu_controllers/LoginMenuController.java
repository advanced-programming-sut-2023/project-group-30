package controller.menu_controllers;

import controller.messages.MenuMessages;

public class LoginMenuController {
    public static MenuMessages userLogin(String username, String password, Boolean stayLoggedIn) {

        return MenuMessages.USER_LOGGED_IN_SUCCESSFULLY;
    }

    public static MenuMessages forgotPassword(String username) {
        return MenuMessages.SECURITY_QUESTION_CONFIRMED;
    }
}
