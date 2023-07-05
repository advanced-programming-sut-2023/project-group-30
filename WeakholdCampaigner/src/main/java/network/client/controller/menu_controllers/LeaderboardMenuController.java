package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Packet;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardMenuController {
    public static ArrayList<HashMap<String, String>> getLeaderboard() {
        Client.clientNetworkComponent.sendPacket(new Packet("getLeaderboard", new HashMap<>()));

        if (!Client.clientNetworkComponent.readLine().equals("Sending"))
            throw new RuntimeException();

        ArrayList<HashMap<String, String>> leaderboard = new ArrayList<>();
        while (true) {
            Packet result = Client.clientNetworkComponent.readPacket();
            if (result.command.equals("Done"))
                break;

            leaderboard.add(result.arguments);
        }

        return leaderboard;
    }

    public static void randomizeUserScores() {
        Client.clientNetworkComponent.sendPacket(new Packet("randomizeUserScores", new HashMap<>()));

        String result = Client.clientNetworkComponent.readLine();
        if (!result.equals("Success"))
            throw new RuntimeException();
    }
}
