package model;

public class PasswordRecoveryQNA {
    private String question;
    private String answer;

    public PasswordRecoveryQNA(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
