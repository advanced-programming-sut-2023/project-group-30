package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.PasswordRecoveryQNA;
import model.User;
import view.ParsedLine;
import view.menus.AppMenu;
import view.utils.MenuUtils;

import java.util.*;
import java.util.regex.Pattern;

public class SignupMenuController extends MenuController {
    static PasswordRecoveryQNA passwordRecoveryQNA;

    public static MenuMessages deprecatedCreateUser(String username, String password, String passwordConfirm,
                                                    String email, String nickname, String slogan) {
        if (!isUsernameValid(username))
            return MenuMessages.INVALID_USERNAME;
        if (isUsernameTaken(username)) {
            username = Database.generateSimilarUsername(username);
            if (!AppMenu.getOneLine("Error: This username has already been taken. Similar username : " +
                    username + "\nDo you want this username to be yours? Y/N").equals("Y")) {
                return MenuMessages.USERNAME_TAKEN;
            }
        }
        if (Database.getUserByEmail(email.toLowerCase()) != null)
            return MenuMessages.TAKEN_EMAIL;
        if (!isEmailValid(email))
            return MenuMessages.INVALID_EMAIL;
        else email = email.toLowerCase();
        /*if (password.equals("random")) {
            password = SignupMenuController.generateRandomPassword();
            if (!AppMenu.getOneLine("Your random password is: " + password +
                    "\nPlease re-enter your password here: ").equals(password))
                return MenuMessages.WRONG_RANDOM_PASSWORD_REENTERED;
        } else*/ if (!isPasswordStrong(password).equals(MenuMessages.STRONG_PASSWORD)) {
            return isPasswordStrong(password);
        } else if (!password.equals(passwordConfirm))
            return MenuMessages.WRONG_PASSWORD_CONFIRMATION;
        /*if (slogan != null) {
            if (slogan.equals("random")) {
                slogan = getRandomSlogan();
                AppMenu.show("your slogan: " + slogan);
            }
        }*/

        //Security question:
        String questionPick = AppMenu.getOneLine("Pick your security question: " +
                "1. What is my father’s name? 2. What\n" +
                "was my first pet’s name? 3. What is my mother’s last name?");
        ParsedLine parsedLine = ParsedLine.parseLine(questionPick);
        HashMap<String, String> options = parsedLine.options;
        String questionNumber = null, answer = null, answerConfirm = null;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-q":
                    questionNumber = argument;
                    break;
                case "-a":
                    answer = argument;
                    break;
                case "-c":
                    answerConfirm = argument;
                    break;
                default:
                    return MenuMessages.WRONG_SECURITY_QUESTION_FORMAT;
            }
        }
        if (checkInvalidSecurityQuestion(questionNumber, answer, answerConfirm) != MenuMessages.OK)
            return checkInvalidSecurityQuestion(questionNumber, answer, answerConfirm);
        else {
            int questionNumberInt = Integer.parseInt(questionNumber);
            passwordRecoveryQNA = new PasswordRecoveryQNA(Database.getSecurityQuestions().get(questionNumberInt - 1)
                    .getQuestion(), answer);
        }
        User user = new User(username, sha256(password), nickname, email, slogan, passwordRecoveryQNA);
        Database.addUser(user);
        return MenuMessages.USER_CREATED_SUCCESSFULLY;
    }

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

    public static MenuMessages checkInvalidSecurityQuestion(String questionNumber, String answer, String answerConfirm) {
        Pattern pattern = Pattern.compile("[1-3]");
        if (!MenuUtils.checkInputsAreNotNull(questionNumber, answer, answerConfirm))
            return MenuMessages.WRONG_SECURITY_QUESTION_FORMAT;
        else if (!pattern.matcher(questionNumber).matches())
            return MenuMessages.OUT_OF_BOUNDS;
        else if (!answer.equals(answerConfirm))
            return MenuMessages.WRONG_ANSWER_CONFIRM;
        return MenuMessages.OK;
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
