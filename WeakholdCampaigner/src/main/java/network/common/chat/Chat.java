package network.common.chat;

import java.util.ArrayList;

public class Chat {
    public enum Type {
        PUBLIC_CHAT,
        PRIVATE_CHAT,
        ROOM
    }
    public final Type type;
    public final ArrayList<String> users;
    private final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private String name;

    public Chat(Type type) {
        this.type = type;

        this.users = new ArrayList<>();

        name = "NA";

        chatMessages.add(new ChatMessage("me", "69:85", "", "this is a test"));
    }

    public boolean addUser(String user) {
        users.add(user);
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(String user) {
        if (this.type.equals(Type.PRIVATE_CHAT)) {
            String firstUser = this.users.get(0);
            return (firstUser.equals(user)) ? this.users.get(1) : firstUser;
        }

        return this.name;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void addMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
    }
}
