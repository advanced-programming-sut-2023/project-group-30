package network.server.controller.menu_controllers;

import network.server.controller.MainController;
import network.common.messages.MenuMessages;
import network.server.Database;
import network.server.model.User;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


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

    public static String sendFriendRequest(String username) {
        User user = Database.getUserByName(username);
        if (user == null)
            return "This username does not exist.";

        User currentUser = MainController.getCurrentUser();
        if (currentUser.getUsername().equals(username))
            return "Cannot send a friend request to self.";

        //currentUser.addFriendRequest(username);
        user.addFriendRequest(currentUser.getUsername());

        return "Success.";
    }

    public static HashMap<String, String> getFriends() {
        HashMap<String, String> friends = new HashMap<>();

        int friendCounter = 1;
        for (String username :
                MainController.getCurrentUser().getFriends()) {
            friends.put(String.valueOf(friendCounter++), username);
        }

        return friends;
    }

    public static HashMap<String, String> getFriendRequests() {
        HashMap<String, String> friends = new HashMap<>();

        int friendCounter = 1;
        for (String username :
                MainController.getCurrentUser().getFriendRequests()) {
            friends.put(String.valueOf(friendCounter++), username);
        }

        return friends;
    }

    public static String resolveFriendRequest(Boolean accept, String username) {
        User otherUser = Database.getUserByName(username);
        if (otherUser == null)
            return "This username does not exist.";

        User currentUser = MainController.getCurrentUser();
        if (!currentUser.removeFromFriendRequests(username))
            return "Something went wrong.";

        if (accept) {
            currentUser.addFriend(username);
            otherUser.addFriend(currentUser.getUsername());
        }

        return "Success";
    }
}
