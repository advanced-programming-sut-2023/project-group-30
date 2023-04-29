package controller;

import controller.menu_controllers.GameMenuController;
import model.Database;
import model.Government;
import model.User;
import view.menus.AppMenu;

import java.util.HashMap;
import java.util.Scanner;

public class MainController {
    private static final HashMap<AppMenu.MenuName, AppMenu> menus = new HashMap<>();
    private static AppMenu currentMenu;
    private static User currentUser;
    private final Scanner scanner = new Scanner(System.in);

    public void initializeApp() {
        menus.put(AppMenu.MenuName.SIGNUP_MENU, AppMenu.getMenu(AppMenu.MenuName.SIGNUP_MENU, scanner));
        menus.put(AppMenu.MenuName.LOGIN_MENU, AppMenu.getMenu(AppMenu.MenuName.LOGIN_MENU, scanner));
        menus.put(AppMenu.MenuName.GAME_MENU, AppMenu.getMenu(AppMenu.MenuName.GAME_MENU, scanner));
        menus.put(AppMenu.MenuName.MAP_MENU, AppMenu.getMenu(AppMenu.MenuName.MAP_MENU, scanner));
        menus.put(AppMenu.MenuName.MAIN_MENU, AppMenu.getMenu(AppMenu.MenuName.MAIN_MENU, scanner));
        menus.put(AppMenu.MenuName.PROFILE_MENU, AppMenu.getMenu(AppMenu.MenuName.PROFILE_MENU, scanner));
        menus.put(AppMenu.MenuName.TRAD_MENU, AppMenu.getMenu(AppMenu.MenuName.TRAD_MENU, scanner));

        currentMenu = menus.get(AppMenu.MenuName.LOGIN_MENU);
        currentUser = null;

        Database.loadData();
    }

    public void run() {
        while (currentMenu != null) {
            currentMenu.run();
        }
    }

    public static void setCurrentMenu(AppMenu.MenuName menuName) {
        currentMenu = menus.get(menuName);
    }

    public static AppMenu getCurrentMenu() {
        return currentMenu;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        MainController.currentUser = currentUser;
    }
}
