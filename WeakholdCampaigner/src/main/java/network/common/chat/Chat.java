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

        //chatMessages.add(new ChatMessage("me", "69:85", "", "this is a test"));
    }

    public boolean addUser(String user) {
        if (users.contains(user)) return false;

        users.add(user);
        return true;
    }

    public void setName(String name) {
        //todo check the name to be unique
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

    public int getNewMessageID() {
        return this.chatMessages.size() + 1;
    }

    public ChatMessage getMessageByID(Integer messageID) {
        for (ChatMessage message :
                this.chatMessages) {
            if (messageID.equals(message.IDLocalToChat)) {
                return message;
            }
        }

        return null;
    }

    public Boolean deleteMessage(Integer messageID) {
        ChatMessage message = getMessageByID(messageID);
        if (message == null) return false;

        this.chatMessages.remove(message);
        return true;
    }
}
