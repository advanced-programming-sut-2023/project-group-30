package network.server.model;

import network.common.chat.Chat;
import network.server.controller.menu_controllers.ChatMenuController;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String slogan;
    private Integer highScore = 0;
    private PasswordRecoveryQNA securityQuestion;
    private String avatarURL;
    private final ArrayList<Chat> chats;
    private Chat currentChat;


    public String getNickname() {
        return nickname;
    }

    public String getSlogan() {
        return slogan;
    }

    public User(String username, String password, String nickname, String email, String slogan
            , PasswordRecoveryQNA securityQuestion, String avatarURL) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.slogan = slogan;
        this.securityQuestion = securityQuestion;
        this.avatarURL = avatarURL;

        this.chats = new ArrayList<>();
        chats.add(ChatMenuController.makePublicChat(username));
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

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getAvatarURL() {
        return this.avatarURL;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
}