package network.common.chat;

public class ChatMessage {
    public final String sender, sentTime, senderAvatarURL;
    //todo: must not store the AvatarURL, rather an Image or ImagePattern. also do something so that if a user changes
    // their avatar, the messages get updated.
    private String message;
    public final int IDLocalToChat;
    private boolean seen = false;

    public ChatMessage(String sender, String sentTime, String senderAvatarURL, String message, int ID) {
        this.sender = sender;
        this.sentTime = sentTime;
        this.senderAvatarURL = senderAvatarURL;
        this.message = message;
        this.IDLocalToChat = ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSeen() {
        this.seen = true;
    }

    public boolean isSeen() {
        return seen;
    }
}
