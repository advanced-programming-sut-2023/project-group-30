package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.User;
import view.Command;

public class ProfileMenuController {
    public static void showProfile(){

    }
    public static MenuMessages changeUsername(String username){
        return MenuMessages.USERNAME_HAS_CHANGED;
    }
    public static MenuMessages changeNickname(String nickname){
        return MenuMessages.NICKNAME_HAS_CHANGED;
    }
    public static MenuMessages changePassword(Command command){
        return MenuMessages.PASSWORD_HAS_CHANGED;
    }
    public static MenuMessages changeEmail(String email){
        return MenuMessages.EMAIL_HAS_CHANGED;
    }
    public static MenuMessages changeSlogan(String slogan){
        return MenuMessages.SLOGAN_HAS_CHANGED;
    }
    //public static MenuMessages getRank()
}
