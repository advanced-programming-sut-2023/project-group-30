package controller.menu_controllers;

import controller.messages.MenuMessages;

public class SignupMenuController {
    public static MenuMessages createUser(String username, String password, String passwordConfirm,
                                          String email, String nickname, String slogan) {
        return MenuMessages.USER_CREATED_SUCCESSFULLY;
    }
}
