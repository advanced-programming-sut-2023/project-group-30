package controller.menu_controllers;

import controller.messages.MenuMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController {
    protected static boolean isUsernameValid(String username) {
        String validUsernameReg = "^[a-zA-Z0-9_]+$";
        Pattern usernamePattern = Pattern.compile(validUsernameReg);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.matches();
    }

    protected static MenuMessages isPasswordStrong(String password) {
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

    protected static boolean isEmailValid(String email) {
        String validEmaileReg = "^[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[a-zA-Z0-9._]+$";
        Pattern emailPattern = Pattern.compile(validEmaileReg);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }
}
