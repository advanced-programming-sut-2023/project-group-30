package network.server.controller.menu_controllers;

import network.common.messages.MenuMessages;
import network.server.Database;
import network.server.model.PasswordRecoveryQNA;
import network.server.model.User;

import java.util.*;

public class SignupMenuController extends MenuController {
    static PasswordRecoveryQNA passwordRecoveryQNA;

    public static void newCreateUser(String username, String password, String email, String nickname,
                                      String slogan, int securityQ, String securityA, String avatarURL) {

        passwordRecoveryQNA = new PasswordRecoveryQNA(Database.getSecurityQuestions().get(securityQ)
                .getQuestion(), securityA);

        User user = new User(username, sha256(password), nickname, email.toLowerCase(), slogan,
                passwordRecoveryQNA, avatarURL);
        Database.addUser(user);
    }

    public static MenuMessages validateUserCreation(String username, String password, String passwordConfirm,
                                                    String email) {
        if (!isUsernameValid(username))
            return MenuMessages.INVALID_USERNAME;
        if (isUsernameTaken(username)) {
            /*todo
            username = Database.generateSimilarUsername(username);
            if (!AppMenu.getOneLine("Error: This username has already been taken. Similar username : " +
                    username + "\nDo you want this username to be yours? Y/N").equals("Y")) {
                return MenuMessages.USERNAME_TAKEN;
            }*/
            return MenuMessages.USERNAME_TAKEN;
        }
        if (Database.getUserByEmail(email.toLowerCase()) != null)
            return MenuMessages.TAKEN_EMAIL;
        if (!isEmailValid(email))
            return MenuMessages.INVALID_EMAIL;

        if (!isPasswordStrong(password).equals(MenuMessages.STRONG_PASSWORD)) {
            return isPasswordStrong(password);
        } else if (!password.equals(passwordConfirm))
            return MenuMessages.WRONG_PASSWORD_CONFIRMATION;

        return MenuMessages.SUCCESS;
    }


    public static String generateRandomPassword() {
        int length = 8;
        final String lowercases = "abcdefghijklmnopqrstuvwxyz";
        final String uppercases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String numbers = "0123456789";
        final String characters = "=+_)(*&^%$#@!~{}[]|/?':;,<>.";
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder(length);
        passwordBuilder.append(lowercases.charAt(random.nextInt(lowercases.length())));
        passwordBuilder.append(uppercases.charAt(random.nextInt(uppercases.length())));
        passwordBuilder.append(numbers.charAt(random.nextInt(numbers.length())));
        passwordBuilder.append(characters.charAt(random.nextInt(characters.length())));
        for (int i = 4; i < length; i++) {
            String allAllowedChar = lowercases + uppercases + numbers + characters;
            passwordBuilder.append(allAllowedChar.charAt(random.nextInt(allAllowedChar.length())));
        }

        return passwordBuilder.toString();
    }

    public static String getRandomSlogan() {
        Random random = new Random();
        return Database.getSlogans().get(random.nextInt(8));
    }

    public static ArrayList<String> getAllSlogans() {
        return Database.getSlogans();
    }

    public static boolean isUsernameTaken(String username) {
        return Database.getUserByName(username) != null;
    }
}
