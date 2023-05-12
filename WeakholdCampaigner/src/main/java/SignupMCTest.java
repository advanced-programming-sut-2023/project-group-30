import controller.menu_controllers.SignupMenuController;
import controller.messages.MenuMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SignupMCTest {
    @Test
    public void invalidUsername(){
        Assertions.assertFalse(SignupMenuController.isUsernameValid("Mamad82!"));
    }

    @Test
    public void invalidChaaracterForEmail(){
        Assertions.assertFalse(SignupMenuController.isEmailValid("mam!ad@gmail.com"));
    }

    @Test
    public void emailWithoutAtSignCharacter(){
        Assertions.assertFalse(SignupMenuController.isEmailValid("mamamgamil.com"));
    }

    @Test
    public void emailWithoutDotCharacter(){
        Assertions.assertFalse(SignupMenuController.isEmailValid("mamad@gamilcom"));
    }

    @Test
    public void correctEmail(){
        Assertions.assertTrue(SignupMenuController.isEmailValid("mamad@gamil.com"));
    }

    @Test
    public void shortPasswordStrong(){
        MenuMessages message = SignupMenuController.isPasswordStrong("sa12");
        Assertions.assertEquals(message, MenuMessages.FEW_CHARACTERS);
    }

    @Test
    public void noLowerCasePassword(){
        MenuMessages message = SignupMenuController.isPasswordStrong("DAWE23!S");
        Assertions.assertEquals(message, MenuMessages.N0_LOWERCASE_LETTER);
    }

    @Test
    public void noUpperCasePassword(){
        MenuMessages message = SignupMenuController.isPasswordStrong("daew23!s");
        Assertions.assertEquals(message, MenuMessages.N0_UPPERCASE_LETTER);
    }

    @Test
    public void noNumberPassword(){
        MenuMessages message = SignupMenuController.isPasswordStrong("DAsac!S");
        Assertions.assertEquals(message, MenuMessages.N0_NUMBER);
    }

    @Test
    public void noCharacterPassword(){
        MenuMessages message = SignupMenuController.isPasswordStrong("DAWe23sS");
        Assertions.assertEquals(message, MenuMessages.NO_NON_WORD_NUMBER_CHARACTER);
    }

    @Test
    public void wrongPasswordConfirmation(){
        MenuMessages message = SignupMenuController.createUser("Mamadam", "Sa23!s",
                "Ssa", "mamad@gmail.com", "feri", null);
        Assertions.assertEquals(MenuMessages.WRONG_PASSWORD_CONFIRMATION, message);
    }

    @Test
    public void strongPassword(){
        MenuMessages message = SignupMenuController.isPasswordStrong("ans@#23AD");
        Assertions.assertEquals(MenuMessages.STRONG_PASSWORD, message);
    }

    @Test
    public void wrongSecurityQuestionFormat(){
        MenuMessages message = SignupMenuController.checkInvalidSecurityQuestion(null ,
                "mamad", "mamad");
        Assertions.assertEquals(MenuMessages.WRONG_SECURITY_QUESTION_FORMAT, message);
    }
    @Test
    public void outOfBoundNumberSecurityQuestion(){
        MenuMessages message = SignupMenuController.checkInvalidSecurityQuestion("5" ,
                "mamad", "mamad");
        Assertions.assertEquals(MenuMessages.OUT_OF_BOUNDS, message);
    }
    @Test
    public void wrongConfirmationSecurityQuestion(){
        MenuMessages message = SignupMenuController.checkInvalidSecurityQuestion("1" ,
                "mamad", "ali");
        Assertions.assertEquals(MenuMessages.WRONG_ANSWER_CONFIRM, message);
    }
    @Test
    public void checkingGenerateRandomPassword(){
        String generatedPassword = SignupMenuController.generateRandomPassword();
        MenuMessages message = SignupMenuController.isPasswordStrong(generatedPassword);
        Assertions.assertEquals(MenuMessages.STRONG_PASSWORD, message);
    }

}
