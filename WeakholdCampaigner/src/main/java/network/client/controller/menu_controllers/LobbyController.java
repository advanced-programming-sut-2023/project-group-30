package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Packet;

import java.util.HashMap;

public class LobbyController {
    public static String createGame(String capacity) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("capacity", capacity);
        Client.clientNetworkComponent.sendPacket(new Packet("createGame", arguments));

        return Client.clientNetworkComponent.readLine();
    }

    public static HashMap<String, String> getGames() {
        HashMap<String, String> arguments = new HashMap<>();
        Client.clientNetworkComponent.sendPacket(new Packet("getGames", arguments));

        Packet result = Client.clientNetworkComponent.readPacket();
        if (!result.command.equals("Games"))
            throw new RuntimeException();

        return result.arguments;
    }
}
