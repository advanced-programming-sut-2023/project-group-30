package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.MainController;
import model.game.Game;
import model.game.map.Map;
import model.game.map.MapCell;
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
    private static HashMap<Integer, Map> maps; //each map must have a unique id.

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

        //TODO: loadMaps, loadGames :
        maps = new HashMap<>();
        maps.put(1, new Map(200));
        games = new HashMap<>();
        games.put(1, new Game(maps.get(1), null, null));
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
        Gson secGson = new Gson();
        Gson thirdGson = new Gson();
        Gson forthGson = new Gson();
        Map map = new Map(200);
        Map map2 = new Map(200);
        Map map3 = new Map(200);
        Map map4 = new Map(200);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.SLATE);
                }
            }

        }
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 15; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.IRON);
                }
            }

        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 30; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.SHALLOW_WATER);
                }
            }

        }
        for (int i = 0; i < 200; i++) {
            for (int j = 29; j < 31; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.RIVER);
                }

            }
        }
        for (int i = 199; i > 170; i--) {
            for (int j = 0; j < 20; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.GRASS);

                }
            }
        }
        for (int i = 199; i > 130; i--) {
            for (int j = 0; j < 25; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.DEEP_WATER);
                }
            }
        }
        for (int i = 199; i > 80; i--) {
            for (int j = 0; j < 30; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.BEACH);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 31; j < 80; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.MEADOW);
                }
            }
        }
        for (int i = 0; i < 40; i++) {
            for (int j = 31; j < 80; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.GRASSLAND);
                }
            }
        }
        for (int i = 0; i < 70; i++) {
            for (int j = 31; j < 100; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.STONE);
                }
            }
        }
        for (int i = 0; i < 80; i++) {
            for (int j = 31; j < 120; j++) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.GRAVEL);
                }
            }
        }
        for (int i = 199; i > 180; i--) {
            for (int j = 199; j > 190; j--) {
                if (map.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map.getCell(i, j).setTexture(MapCell.Texture.OIL);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.DEEP_WATER);
                }
            }

        }
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 15; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.BEACH);
                }
            }

        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 30; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.PLAIN);
                }
            }

        }
        for (int i = 0; i < 200; i++) {
            for (int j = 29; j < 31; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.RIVER);
                }

            }
        }
        for (int i = 199; i > 170; i--) {
            for (int j = 0; j < 20; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.MEADOW);

                }
            }
        }
        for (int i = 199; i > 130; i--) {
            for (int j = 0; j < 25; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.GRASSLAND);
                }
            }
        }
        for (int i = 199; i > 80; i--) {
            for (int j = 0; j < 30; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.STONE);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 31; j < 80; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.IRON);
                }
            }
        }
        for (int i = 0; i < 40; i++) {
            for (int j = 31; j < 80; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.GRASS);
                }
            }
        }
        for (int i = 0; i < 70; i++) {
            for (int j = 31; j < 100; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.GRAVEL);
                }
            }
        }
        for (int i = 0; i < 80; i++) {
            for (int j = 31; j < 120; j++) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.SHALLOW_WATER);
                }
            }
        }
        for (int i = 199; i > 180; i--) {
            for (int j = 199; j > 190; j--) {
                if (map2.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map2.getCell(i, j).setTexture(MapCell.Texture.SLATE);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.DEEP_WATER);
                }
            }

        }
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 15; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.BEACH);
                }
            }

        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 30; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.STONE);
                }
            }

        }
        for (int i = 0; i < 200; i++) {
            for (int j = 29; j < 31; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.GRASSLAND);
                }

            }
        }
        for (int i = 199; i > 170; i--) {
            for (int j = 0; j < 20; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.MEADOW);
                }
            }
        }
        for (int i = 199; i > 130; i--) {
            for (int j = 0; j < 25; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.GRASS);
                }
            }
        }
        for (int i = 199; i > 80; i--) {
            for (int j = 0; j < 30; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.SHALLOW_WATER);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 31; j < 80; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.SLATE);
                }
            }
        }
        for (int i = 0; i < 40; i++) {
            for (int j = 31; j < 80; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.OIL);
                }
            }
        }
        for (int i = 0; i < 70; i++) {
            for (int j = 31; j < 100; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.IRON);
                }
            }
        }
        for (int i = 0; i < 80; i++) {
            for (int j = 31; j < 120; j++) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.RIVER);
                }
            }
        }
        for (int i = 199; i > 180; i--) {
            for (int j = 199; j > 190; j--) {
                if (map3.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map3.getCell(i, j).setTexture(MapCell.Texture.GRAVEL);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.RIVER);
                }
            }

        }
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 15; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.OIL);
                }
            }

        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 30; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.PLAIN);
                }
            }

        }
        for (int i = 0; i < 200; i++) {
            for (int j = 29; j < 31; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.STONE);
                }

            }
        }
        for (int i = 199; i > 170; i--) {
            for (int j = 0; j < 20; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.GRASS);

                }
            }
        }
        for (int i = 199; i > 130; i--) {
            for (int j = 0; j < 25; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.GRASSLAND);
                }
            }
        }
        for (int i = 199; i > 80; i--) {
            for (int j = 0; j < 30; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.SLATE);
                }
            }
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 31; j < 80; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.IRON);
                }
            }
        }
        for (int i = 0; i < 40; i++) {
            for (int j = 31; j < 80; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.DEEP_WATER);
                }
            }
        }
        for (int i = 0; i < 70; i++) {
            for (int j = 31; j < 100; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.BEACH);
                }
            }
        }
        for (int i = 0; i < 80; i++) {
            for (int j = 31; j < 120; j++) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.MEADOW);
                }
            }
        }
        for (int i = 199; i > 180; i--) {
            for (int j = 199; j > 190; j--) {
                if (map4.getCell(i, j).getTexture().equals(MapCell.Texture.LAND)) {
                    map4.getCell(i, j).setTexture(MapCell.Texture.SHALLOW_WATER);
                }
            }
        }
        String json = gson.toJson(gson);
        String secJson = gson.toJson(secGson);
        String thirdJson = gson.toJson(thirdGson);
        String forthJson = gson.toJson(forthGson);
        try {
            FileWriter writer = new FileWriter("src/main/resources/firstDefaultMap.json");
            writer.write(json);
            writer.close();
            FileWriter secWriter = new FileWriter("src/main/resources/secondDefaultMap.json");
            secWriter.write(secJson);
            secWriter.close();
            FileWriter thirdWriter = new FileWriter("src/main/resources/thirdDefaultMap.json");
            thirdWriter.write(thirdJson);
            thirdWriter.close();
            FileWriter forthWriter = new FileWriter("src/main/resources/forthDefaultMap.json");
            forthWriter.write(forthJson);
            forthWriter.close();
            System.out.println(map3.getCell(3, 103).getTexture());
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