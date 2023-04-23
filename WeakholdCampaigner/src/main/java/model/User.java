package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nikname;
    private String email;
    private String slogan;
    private PasswordRecoveryQNA securityQuestion = new PasswordRecoveryQNA();
    private Integer score = 0;
    private ArrayList<Trad> tradList;
    private ArrayList<Trad> tradHistory;
    private ArrayList<Trad> tradNotification;

    public User(String username, String password, String nikname, String email, String slogan
            , PasswordRecoveryQNA securityQuestion) {
        this.username = username;
        this.password = password;
        this.nikname = nikname;
        this.email = email;
        this.slogan = slogan;
        this.securityQuestion = securityQuestion;
    }

    public String getUsername() {
        return username;
    }

    public void increaseScore(int increaseAmount) {
        score += increaseAmount;
    }

    public Integer getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }
}
