package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String slogan;
    private PasswordRecoveryQNA securityQuestion;
    private Integer score = 0;
    private ArrayList<Trade> tradeList;
    private ArrayList<Trade> tradeHistory;
    private ArrayList<Trade> tradeNotification;

    public User(String username, String password, String nickname, String email, String slogan
            , PasswordRecoveryQNA securityQuestion) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
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