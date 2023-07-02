package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Packet;
import network.common.messages.MenuMessages;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


public class ProfileMenuController extends MenuController {
    public static MenuMessages changeUsername(String username) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);

        Client.clientNetworkComponent.sendPacket(new Packet("changeUsername", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }

    public static MenuMessages changeNickname(String nickname) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("nickname", nickname);

        Client.clientNetworkComponent.sendPacket(new Packet("changeNickname", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }

    public static MenuMessages changePassword(String oldPassword, String newPassword) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("oldPassword", oldPassword);
        arguments.put("newPassword", newPassword);

        Client.clientNetworkComponent.sendPacket(new Packet("changePassword", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }

    public static MenuMessages changeEmail(String email) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("email", email);

        Client.clientNetworkComponent.sendPacket(new Packet("changeEmail", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }

    public static MenuMessages changeSlogan(@Nullable String slogan) {
        //todo: check if slogan being null makes a problem
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("slogan", slogan);

        Client.clientNetworkComponent.sendPacket(new Packet("changeSlogan", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }

    public static String getUsername() {
        Client.clientNetworkComponent.sendPacket(new Packet("getUsername", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static String getNickname() {
        Client.clientNetworkComponent.sendPacket(new Packet("getNickname", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static String getEmail() {
        Client.clientNetworkComponent.sendPacket(new Packet("getEmail", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static String getAvatarURL() {
        Client.clientNetworkComponent.sendPacket(new Packet("getAvatarURL", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static void setAvatarURL(String selectedAvatarURL) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("selectedAvatarURL", selectedAvatarURL);

        Client.clientNetworkComponent.sendPacket(new Packet("setAvatarURL", arguments));

        String result = Client.clientNetworkComponent.readLine();
        if (!result.equals("Success"))
            throw new RuntimeException();
    }

    public static String getSlogan() {
        Client.clientNetworkComponent.sendPacket(new Packet("getSlogan", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }
}
