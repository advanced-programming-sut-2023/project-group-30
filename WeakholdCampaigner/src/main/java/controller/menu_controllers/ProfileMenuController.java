package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.User;
import org.jetbrains.annotations.Nullable;
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
    public static MenuMessages changePassword(String oldpassword, String newpassword ){
        return MenuMessages.PASSWORD_HAS_CHANGED;
    }
    public static MenuMessages changeEmail(String email){
        return MenuMessages.EMAIL_HAS_CHANGED;
    }
    public static MenuMessages changeSlogan(@Nullable String slogan){
        return MenuMessages.SLOGAN_HAS_CHANGED;
    }
    public static Integer displayHighscore(){
        Integer score = 0;
        return score;
    }
    public static Integer displayRank(){
        Integer rank = 0;
        return rank;
    }
    public static MenuMessages displaySlogan(){
        return MenuMessages.DISPLAY;
    }
    public static MenuMessages displayProfile(){
        return MenuMessages.DISPLAY;
    }
}
