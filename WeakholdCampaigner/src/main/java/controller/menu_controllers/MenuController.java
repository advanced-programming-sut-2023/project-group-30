package controller.menu_controllers;

import controller.messages.MenuMessages;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController {
    protected static boolean isUsernameValid(String username) {
        String validUsernameReg = "^[a-zA-Z0-9_]+$";
        Pattern usernamePattern = Pattern.compile(validUsernameReg);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.matches();
    }


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

    protected static boolean isEmailValid(String email) {
        String validEmaileReg = "^[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[a-zA-Z0-9._]+$";
        Pattern emailPattern = Pattern.compile(validEmaileReg);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    public static String getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] cryptogtaphicBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        BigInteger number = new BigInteger(1, cryptogtaphicBytes);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }//TODO: use this method in logic and profile and signup controller
}