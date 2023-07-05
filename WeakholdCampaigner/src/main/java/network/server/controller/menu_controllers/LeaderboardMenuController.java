package network.server.controller.menu_controllers;

import network.server.Database;
import network.server.model.User;

import java.util.*;

public class LeaderboardMenuController {
    public static ArrayList<HashMap<String, String>> getLeaderboard() {
        ArrayList<HashMap<String, String>> leaderboard = new ArrayList<>();

        List<User> sortedUsers = new ArrayList<>(Database.getAllUsers());
        sortedUsers.sort(Comparator.comparing(User::getScore));
        Collections.reverse(sortedUsers);

        for (User user :
                sortedUsers) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", user.getUsername());
            hashMap.put("avatarURL", user.getAvatarURL());
            hashMap.put("score", user.getScore().toString());
            hashMap.put("lastSeen", user.getLastSeen());

            leaderboard.add(hashMap);
        }
        
        return leaderboard;
    }

    public static void randomizeUserScores() {
        for (User user :
                Database.getAllUsers()) {
            user.setScore();
        }
    }
}
