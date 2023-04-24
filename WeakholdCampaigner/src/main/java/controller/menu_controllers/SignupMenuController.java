package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupMenuController {
    public static MenuMessages createUser(String username, String password, String passwordConfirm,
                                          String email, String nickname, String slogan) {
        if (!isUsernameValid(username))
            return MenuMessages.INVALID_USERNAME;
        if (Database.getUserByName(username) != null)
            return MenuMessages.TAKEN_USERNAME;
        if (Database.getUserByEmail(email.toLowerCase()) != null)
            return MenuMessages.TAKEN_EMAIL;
        if (!isEmailValid(email))
            return MenuMessages.INVALID_EMAIL;
        else email = email.toLowerCase();
        if (password.equals("random")) {
            return MenuMessages.RANDOM_PASSWORD;
        } else if (!isPasswordStrong(password).equals(MenuMessages.STRONG_PASSWORD)) {
            return isPasswordStrong(password);
        } else if (!password.equals(passwordConfirm))
            return MenuMessages.WRONG_PASSWORD_CONFIRMATION;
//        if(slogan.equals("random"))
//            slogan = Database.getSlogans().;
        return MenuMessages.USER_CREATED_SUCCESSFULLY;//TODO: return callligraphic password
    }

    private static boolean isUsernameValid(String username) {
        String validUsernameReg = "^[a-zA-Z0-9_]+$";
        Pattern usernamePattern = Pattern.compile(validUsernameReg);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.matches();
    }

    private static boolean isEmailValid(String email) {
        String validEmaileReg = "^[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[a-zA-Z0-9._]+$";
        Pattern emailPattern = Pattern.compile(validEmaileReg);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

//    public static String suggestingUsername(String username) {
//        int addedNumberToUsername = 0;
//        while (Database.getUserByName())
//        return username;
//    }

    public static String generateRandomPassword() {
        int length = 8;
        final String lowercases = "abcdefghijklmnopqrstuvwxyz";
        final String uppercases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String numbers = "0123456789";
        final String characters = "=+-_)(*&^%$#@!~{}[]|/?':;,<>.\"";
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder(length);
        passwordBuilder.append(lowercases.charAt(random.nextInt(lowercases.length())));
        passwordBuilder.append(uppercases.charAt(random.nextInt(uppercases.length())));
        passwordBuilder.append(numbers.charAt(random.nextInt(numbers.length())));
        passwordBuilder.append(characters.charAt(random.nextInt(characters.length())));
        for(int i = 4;i < length;i++){
            String allAllowedChar = lowercases + uppercases + numbers + characters;
            passwordBuilder.append(allAllowedChar.charAt(random.nextInt(allAllowedChar.length())));
        }

        return passwordBuilder.toString();
    }

    private static MenuMessages isPasswordStrong(String password) {
        if (password.length() < 6) return MenuMessages.FEW_CHARACTERS;
        else {
            String strongPasswordReg = "^(?=.*(?<lowercase>[a-z]*))(?=.*(?<uppercase>[A-Z]*))" +
                    "(?=.*(?<number>\\d*))(?=.*(?<character>[^a-zA-Z0-9]*)).+$";
            Pattern strongPasswordpattern = Pattern.compile(strongPasswordReg);
            Matcher strongPasswordMatcher = strongPasswordpattern.matcher(password);
            if (strongPasswordMatcher.group("lowercase").length() == 0) {
                return MenuMessages.N0_LOWERCASE_LETTER;
            } else if (strongPasswordMatcher.group("uppercase").length() == 0) {
                return MenuMessages.N0_UPPERCASE_LETTER;
            } else if (strongPasswordMatcher.group("number").length() == 0) {
                return MenuMessages.N0_NUMBER;
            } else if (strongPasswordMatcher.group("character").length() == 0) {
                return MenuMessages.NO_NON_WORD_NUMBER_CHARACTER;
            } else {
                return MenuMessages.STRONG_PASSWORD;
            }
        }
    }
}
