package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Packet;
import network.common.chat.Chat;
import network.server.controller.MainController;
import network.server.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatMenuController {
    public static ArrayList<String> getPrivateChats() {
        Client.clientNetworkComponent.sendPacket(new Packet("getPrivateChats", new HashMap<>()));

        Packet result = Client.clientNetworkComponent.readPacket();
        return new ArrayList<>(result.arguments.values());
    }

    public static ArrayList<String> getRooms() {
        Client.clientNetworkComponent.sendPacket(new Packet("getRooms", new HashMap<>()));

        Packet result = Client.clientNetworkComponent.readPacket();
        return new ArrayList<>(result.arguments.values());
    }

    public static ArrayList<String> getPublicChats() {
        Client.clientNetworkComponent.sendPacket(new Packet("getPublicChats", new HashMap<>()));

        Packet result = Client.clientNetworkComponent.readPacket();
        return new ArrayList<>(result.arguments.values());
    }

    public static Chat getChat(String type, String name) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("type", type);
        arguments.put("name", name);
        Client.clientNetworkComponent.sendPacket(new Packet("getChat", arguments));

        return Client.clientNetworkComponent.readChat();
    }

    public static void sendChatMessage(String message) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("message", message);
        Client.clientNetworkComponent.sendPacket(new Packet("sendChatMessage", arguments));

        String result = Client.clientNetworkComponent.readLine();
        if (!result.equals("Success")) {
            throw new RuntimeException();
        }
        //System.out.println("message sent");
    }

    public static String makePrivateChat(String username) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        Client.clientNetworkComponent.sendPacket(new Packet("makePrivateChat", arguments));

        return Client.clientNetworkComponent.readLine();
    }

    public static String makeRoom(String roomName) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("roomName", roomName);
        Client.clientNetworkComponent.sendPacket(new Packet("makeRoom", arguments));

        return Client.clientNetworkComponent.readLine();
    }

    public static String addMember(String username) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        Client.clientNetworkComponent.sendPacket(new Packet("addMember", arguments));

        return Client.clientNetworkComponent.readLine();
    }

    public static Boolean canEditMessage(Integer messageID) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("messageID", messageID.toString());
        Client.clientNetworkComponent.sendPacket(new Packet("canEditMessage", arguments));

        return Boolean.parseBoolean(Client.clientNetworkComponent.readLine());
    }

    public static String editMessage(Integer messageID, String newMessage) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("messageID", messageID.toString());
        arguments.put("newMessage", newMessage);
        Client.clientNetworkComponent.sendPacket(new Packet("editMessage", arguments));

        return Client.clientNetworkComponent.readLine();
    }

    public static String deleteMessage(Integer messageID) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("messageID", messageID.toString());
        Client.clientNetworkComponent.sendPacket(new Packet("deleteMessage", arguments));

        return Client.clientNetworkComponent.readLine();
    }
}
