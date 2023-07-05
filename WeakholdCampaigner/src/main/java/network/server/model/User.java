package network.server.model;

import network.common.chat.Chat;
import network.server.controller.menu_controllers.ChatMenuController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

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
    private int score;
    private String lastSeen;
    private final ArrayList<String> friends = new ArrayList<>();
    private final ArrayList<String> friendRequests = new ArrayList<>();


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
        setScore();
        this.lastSeen = "Never";
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

    public boolean addChat(Chat chatToAdd) {
        String newChatName = chatToAdd.getName(username);
        Chat alreadyExists = null;
        for (Chat existingChat :
                this.chats) {
            if (existingChat.getName(username).equals(newChatName)) {
                alreadyExists = existingChat;
                break;
            }
        }

        if (alreadyExists != null) {
            this.chats.remove(alreadyExists);
            this.chats.add(chatToAdd);
            return false;
        }

        this.chats.add(chatToAdd);
        return true;
    }

    public Integer getScore() {
        return score;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void toggleLastSeen() {
        if (this.lastSeen.equals("Online")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);

            lastSeen = time;
            return;
        }

        this.lastSeen = "Online";
    }

    public void setOnline() {
        this.lastSeen = "Online";
    }

    public void setScore() {
        this.score = (new Random()).nextInt(100000);
    }

    public boolean addFriend(String username) {
        if (this.friends.size() >= 100)
            return false;

        this.friends.add(username);
        return true;
    }

    public boolean addFriendRequest(String username) {
        this.friendRequests.add(username);
        return true;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }

    public boolean removeFromFriendRequests(String username) {
        return this.friendRequests.remove(username);
    }
}