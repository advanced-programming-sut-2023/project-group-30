package controller.menu_controllers;

import controller.messages.MenuMessages;

import java.util.regex.Pattern;

public class MenuController {
    public static MenuMessages isPasswordStrong(String password) {
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
