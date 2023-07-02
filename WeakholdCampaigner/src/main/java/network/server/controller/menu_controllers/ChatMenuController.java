package network.server.controller.menu_controllers;

import network.common.chat.Chat;
import network.common.chat.ChatMessage;
import network.server.controller.MainController;
import network.server.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ChatMenuController {
    public static Chat makePrivateChat(String user1, String user2) {
        Chat chat = new Chat(Chat.Type.PRIVATE_CHAT);
        chat.addUser(user1);
        chat.addUser(user2);
        return chat;
    }

    public static Chat makeRoom(String owner, String name) {
        Chat chat = new Chat(Chat.Type.ROOM);
        chat.addUser(owner);
        chat.setName(name);

        return chat;
    }

    private static Chat publicChat;
    static {
        publicChat = new Chat(Chat.Type.PUBLIC_CHAT);
        publicChat.setName("Public Chat");

        try {
            for (Chat chat :
                    MainController.getCurrentUser().getChats()) {
                if (chat.type.equals(Chat.Type.PUBLIC_CHAT)) {
                    publicChat = chat;
                    break;
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
                    return chat;
                }
            }
        }

        throw new RuntimeException();
    }

    public static boolean sendChatMessage(String message) {
        System.out.println("message received");
        User currentUser = MainController.getCurrentUser();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);

        currentUser.getCurrentChat().addMessage(
                new ChatMessage(currentUser.getUsername(), time, currentUser.getAvatarURL(), message)
        );
        return true;
    }
}
