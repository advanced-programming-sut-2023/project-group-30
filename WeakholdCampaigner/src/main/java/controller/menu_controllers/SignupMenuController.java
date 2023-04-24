package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import view.AppMenu;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupMenuController {
    public static MenuMessages createUser(String username, String password, String passwordConfirm,
                                          String email, String nickname, String slogan) {
        if (!isUsernameValid(username))
            return MenuMessages.INVALID_USERNAME;
        if (Database.getUserByName(username) != null) {
            username = Database.generateSimilarUsername(username);
            if (!AppMenu.getOneLine("Error: This username has already been taken. Similar username : " +
                    username+"\nDo you want this username to be yours? Y/N").equals("Y")) {
                return MenuMessages.OPERATION_CANCELLED;
            }
        }
        if (Database.getUserByEmail(email.toLowerCase()) != null)
            return MenuMessages.TAKEN_EMAIL;
        if (!isEmailValid(email))
            return MenuMessages.INVALID_EMAIL;
        else email = email.toLowerCase();
        if (password.equals("random")) {
            password = SignupMenuController.generateRandomPassword();
            if (!AppMenu.getOneLine("Your random password is: " + password +
                    "\nPlease re-enter your password here: ").equals(password))
                return MenuMessages.WRONG_RANDOM_PASSWORD_REENTERED;
        }// else if (!isPasswordStrong(password).equals(MenuMessages.STRONG_PASSWORD)) {
           // return isPasswordStrong(password);
       // }
        else if (!password.equals(passwordConfirm))
            return MenuMessages.WRONG_PASSWORD_CONFIRMATION;
        if(slogan.equals("random")) {
            ;
        }
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
        for (int i = 4; i < length; i++) {
            String allAllowedChar = lowercases + uppercases + numbers + characters;
            passwordBuilder.append(allAllowedChar.charAt(random.nextInt(allAllowedChar.length())));
        }

        return passwordBuilder.toString();
    }

    private static MenuMessages isPasswordStrong(String password) {
        if (password.length() < 6) return MenuMessages.FEW_CHARACTERS;
        else {
            Pattern patternForLowerCase = Pattern.compile(".*[a-z]+.*");
            Pattern patternForUpperCase = Pattern.compile(".*[A-Z]+.*");
            Pattern patternForNumber = Pattern.compile(".*\\d+.*");
            Pattern patternForCharacter = Pattern.compile(".*[^a-zA-Z0-9]+.*");

            if (!patternForLowerCase.matcher(password).matches()) {
                return MenuMessages.N0_LOWERCASE_LETTER;
            } else if (!patternForUpperCase.matcher(password).matches()) {
                return MenuMessages.N0_UPPERCASE_LETTER;
            } else if (!patternForNumber.matcher(password).matches()) {
                return MenuMessages.N0_NUMBER;
            } else if (!patternForCharacter.matcher(password).matches()) {
                return MenuMessages.NO_NON_WORD_NUMBER_CHARACTER;
            } else {
                return MenuMessages.STRONG_PASSWORD;
            }
        }
    }
}
