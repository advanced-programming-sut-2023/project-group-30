package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String slogan;
    private Integer highScore = 0;
    private PasswordRecoveryQNA securityQuestion;


    public String getNickname() {
        return nickname;
    }

    public String getSlogan() {
        return slogan;
    }

    public User(String username, String password, String nickname, String email, String slogan
            , PasswordRecoveryQNA securityQuestion) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.slogan = slogan;
        this.securityQuestion = securityQuestion;
    }

    public Integer getHighScore() {
        return highScore;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public PasswordRecoveryQNA getSecurityQuestion() {
        return securityQuestion;
    }


}