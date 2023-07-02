package network.common.chat;

public class ChatMessage {
    public final String sender, sentTime, senderAvatarURL;
    private String message;

    public ChatMessage(String sender, String sentTime, String senderAvatarURL, String message) {
        this.sender = sender;
        this.sentTime = sentTime;
        this.senderAvatarURL = senderAvatarURL;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
