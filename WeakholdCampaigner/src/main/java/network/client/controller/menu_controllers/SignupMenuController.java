package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Packet;
import network.common.messages.MenuMessages;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupMenuController extends MenuController {

    public static void newCreateUser(String username, String password, String email, String nickname,
                                     String slogan, int securityQ, String securityA, String avatarURL) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        arguments.put("email", email);
        arguments.put("nickname", nickname);
        arguments.put("slogan", slogan);
        arguments.put("securityQ", Integer.toString(securityQ));
        arguments.put("securityA", securityA);
        arguments.put("avatarURL", avatarURL); //todo do something about the avatarURL
        Client.clientNetworkComponent.sendPacket(new Packet("newCreateUser", arguments));

        String result = Client.clientNetworkComponent.readLine();
        if (!result.equals("Success"))
            throw new RuntimeException();
    }

    public static MenuMessages validateUserCreation(String username, String password, String passwordConfirm,
                                                    String email) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        arguments.put("passwordConfirm", passwordConfirm);
        arguments.put("email", email);
        Client.clientNetworkComponent.sendPacket(new Packet("validateUserCreation", arguments));

        String result = Client.clientNetworkComponent.readLine();
        return getMenuMessage(result);
    }


    public static String generateRandomPassword() {
        Client.clientNetworkComponent.sendPacket(new Packet("generateRandomPassword", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static String getRandomSlogan() {
        Client.clientNetworkComponent.sendPacket(new Packet("getRandomSlogan", new HashMap<>()));

        return Client.clientNetworkComponent.readLine();
    }

    public static ArrayList<String> getAllSlogans() {
        Client.clientNetworkComponent.sendPacket(new Packet("getAllSlogans", new HashMap<>()));

        Packet packet = Client.clientNetworkComponent.readPacket();
        if (!packet.command.equals("Slogans"))
            throw new RuntimeException();

        return new ArrayList<>(packet.arguments.values());
    }

}
