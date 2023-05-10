package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.MainController;
import model.game.Game;
import model.game.map.Map;
import org.jetbrains.annotations.Nullable;
import view.menus.AppMenu;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Database {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static ArrayList<PasswordRecoveryQNA> securityQuestions = new ArrayList<>();
    private static ArrayList<String> slogans = new ArrayList<>();
    private static HashMap<Integer, Game> games; //each game must have a unique id.
    private static HashMap<Integer, Map> maps = new HashMap<>(); //each map must have a unique id.

    public static void saveAllUsers() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("src/main/resources/allUsers.json")) {
            gson.toJson(allUsers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        Database.loadSecurityQuestions();
        Database.loadSlogan();
        Database.loadAllUsers();
        Database.loadStayLogin();
        Database.loadMap();

        //TODO: loadGames :
        games = new HashMap<>();
    }

    public static Integer addGame(Game game){
        int id = games.size() + 1;
        games.put(id, game);
        return id;
    }

    private static void loadAllUsers() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/allUsers.json")) {
            BufferedReader br = new BufferedReader(reader);
            Type userListType = new TypeToken<ArrayList<User>>() {
            }.getType();
            allUsers = gson.fromJson(br, userListType);
            if (allUsers == null)
                allUsers = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSecurityQuestions() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/securityQuestions.json")) {
            BufferedReader br = new BufferedReader(reader);
            Type QNAListType = new TypeToken<ArrayList<PasswordRecoveryQNA>>() {
            }.getType();
            securityQuestions = gson.fromJson(br, QNAListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMap() {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("src/main/resources/defaultMap.json");
            Map firstMap = gson.fromJson(reader, Map.class);
            maps.put(1, firstMap);
            FileReader secondReader = new FileReader("src/main/resources/secondDefaultMap.json");
            Map secondMap = gson.fromJson(secondReader, Map.class);
            maps.put(2, secondMap);
            FileReader thirdReader = new FileReader("src/main/resources/thirdDefaultMap.json");
            Map thirdMap = gson.fromJson(secondReader, Map.class);
            maps.put(3, thirdMap);
            FileReader forthReader = new FileReader("src/main/resources/forthDefaultMap.json");
            Map forthMap = gson.fromJson(forthReader, Map.class);
            maps.put(4, forthMap);
            FileReader fifthReader = new FileReader("src/main/resources/fifthDefaultMap.json");
            Map fifthMap = gson.fromJson(fifthReader, Map.class);
            maps.put(5, fifthMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadSlogan() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/slogan.json")) {
            BufferedReader br = new BufferedReader(reader);
            Type StringListType = new TypeToken<ArrayList<String>>() {
            }.getType();
            slogans = gson.fromJson(br, StringListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static User getUserByName(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    @Nullable
    public static User getUserByEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    @Nullable
    public static Game getGameById(int id) {
        return games.get(id);
    }

    @Nullable
    public static Map getMapById(int id) {
        return maps.get(id);
    }


    public static ArrayList<User> sortUserByRank() {
        ArrayList<User> sortedUserByRank = new ArrayList<>(allUsers);
        Comparator<User> byScore = Comparator.comparing(User::getHighScore);
        sortedUserByRank.sort(byScore);
        Collections.reverse(sortedUserByRank);
        return sortedUserByRank;
    }

    public static String generateSimilarUsername(String username) {
        int addedNumberToUsername = 0;
        String newUsername = username + addedNumberToUsername;
        while (getUserByName(newUsername) != null) {
            addedNumberToUsername++;
            newUsername = username + addedNumberToUsername;
        }
        return newUsername;
    }

    private static void loadStayLogin() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/stayLoginUser.json")) {
            User user = gson.fromJson(reader, User.class);
            if (user != null) {
                MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
                MainController.setCurrentUser(getUserByName(user.getUsername()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveStayLoggedInUser(User user) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("src/main/resources/stayLoginUser.json")) {
            gson.toJson(user, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void addUser(User user) {
        allUsers.add(user);
        saveAllUsers();
    }

    public static ArrayList<PasswordRecoveryQNA> getSecurityQuestions() {
        return securityQuestions;
    }

    public static ArrayList<String> getSlogans() {
        return slogans;
    }

}