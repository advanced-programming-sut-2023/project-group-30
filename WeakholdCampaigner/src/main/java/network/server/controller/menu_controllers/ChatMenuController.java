package network.server.controller.menu_controllers;

import network.common.chat.Chat;
import network.common.chat.ChatMessage;
import network.server.Database;
import network.server.controller.MainController;
import network.server.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ChatMenuController {
    public static String makePrivateChat(String username1, String username2) {
        User user1 = Database.getUserByName(username1), user2 = Database.getUserByName(username2);
        if (user1 == null || user2 == null)
            return "User does not exist.";

        Chat chat = new Chat(Chat.Type.PRIVATE_CHAT);
        chat.addUser(username1);
        chat.addUser(username2);

        user1.addChat(chat);
        user2.addChat(chat);
        return "Success.";
    }

    public static String makeRoom(String owner, String name) {
        User user = Database.getUserByName(owner);
        if (user == null)
            return "User does not exist.";

        Chat chat = new Chat(Chat.Type.ROOM);
        chat.addUser(owner);
        chat.setName(name);

        user.addChat(chat);

        return "Success.";
    }

    private static Chat publicChat;

    static {
        publicChat = new Chat(Chat.Type.PUBLIC_CHAT);
        publicChat.setName("Public Chat");

        try {
            for (User user :
                    Database.getAllUsers()) {
                for (Chat chat :
                        user.getChats())
                    if (chat.type.equals(Chat.Type.PUBLIC_CHAT)) {
                        publicChat = chat;
                        //break;
                        //System.out.println(user.getUsername() + " has a PublicChat.");
                    }
            }
        } catch (NullPointerException ignored) {

        }
    }

    public static Chat makePublicChat(String user) {
        if (!publicChat.users.contains(user))
            publicChat.addUser(user);
        return publicChat;
    }


    public static HashMap<String, String> getPrivateChats() {
        User currentUser = MainController.getCurrentUser();

        HashMap<String, String> privateChats = new HashMap<>();
        for (Chat chat :
                currentUser.getChats()) {
            if (chat.type.equals(Chat.Type.PRIVATE_CHAT))
                privateChats.put(
                        String.valueOf(privateChats.size()),
                        chat.getName(currentUser.getUsername()));
        }

        return privateChats;
    }

    public static HashMap<String, String> getRooms() {
        User currentUser = MainController.getCurrentUser();

        HashMap<String, String> rooms = new HashMap<>();
        for (Chat chat :
                currentUser.getChats()) {
            if (chat.type.equals(Chat.Type.ROOM))
                rooms.put(
                        String.valueOf(rooms.size()),
                        chat.getName(currentUser.getUsername()));
        }

        return rooms;
    }

    public static HashMap<String, String> getPublicChats() {
        User currentUser = MainController.getCurrentUser();

        HashMap<String, String> publicChats = new HashMap<>();
        for (Chat chat :
                currentUser.getChats()) {
            if (chat.type.equals(Chat.Type.PUBLIC_CHAT))
                publicChats.put(
                        String.valueOf(publicChats.size()),
                        chat.getName(currentUser.getUsername()));
        }

        return publicChats;
    }

    public static Chat getChat(String type, String chatName) {
        User currentUser = MainController.getCurrentUser();

        for (Chat chat :
                currentUser.getChats()) {
            if (chat.getName(currentUser.getUsername()).equals(chatName)) {
                if (chat.type.toString().equals(type)) {
                    currentUser.setCurrentChat(chat);
                    makeMessagesSeen(chat, currentUser.getUsername());
                    return chat;
                }
            }
        }

        throw new RuntimeException();
    }

    public static boolean sendChatMessage(String message) {
        //System.out.println("message received");
        User currentUser = MainController.getCurrentUser();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);

        Chat currentChat = currentUser.getCurrentChat();
        currentChat.addMessage(
                new ChatMessage(currentUser.getUsername(), time, currentUser.getAvatarURL(),
                        message, currentChat.getNewMessageID())
        );
        return true;
    }

    public static String addMember(String username) {
        Chat currentChat = MainController.getCurrentUser().getCurrentChat();
        if (!currentChat.type.equals(Chat.Type.ROOM))
            return "Only Rooms support adding members.";

        User user = Database.getUserByName(username);
        if (user == null)
            return "This user does not exist.";

        if (!currentChat.addUser(username)) {
            return "This user is already in the chat.";
        }
        user.addChat(currentChat);

        return "Success.";
    }

    public static Boolean canEditMessage(Integer messageID) {
        User currentUser = MainController.getCurrentUser();
        return
                currentUser.getCurrentChat().getMessageByID(messageID).sender.equals(currentUser.getUsername());
    }

    public static String editMessage(Integer messageID, String newMessage) {
        if (!canEditMessage(messageID))
            return "Access Denied.";

        MainController.getCurrentUser().getCurrentChat().getMessageByID(messageID).setMessage(newMessage);
        return "Success.";
    }

    public static String deleteMessage(Integer messageID) {
        if (!canEditMessage(messageID))
            return "Access Denied.";

        if (!MainController.getCurrentUser().getCurrentChat().deleteMessage(messageID))
            return "Something went wrong.";
        return "Success.";
    }

    private static void makeMessagesSeen(Chat chat, String viewer) {
        for (ChatMessage chatMessage :
                chat.getChatMessages()) {
            if (!chatMessage.sender.equals(viewer))
                chatMessage.setSeen();
        }
    }
}
