package controller;

import model.User;
import view.AppMenu;

import java.util.HashMap;
import java.util.Scanner;

public class MainController {
    private final HashMap<AppMenu.MenuName, AppMenu> menus = new HashMap<>();
    private AppMenu currentMenu;
    private User currentUser;
    private final Scanner scanner = new Scanner(System.in);

    public void initializeApp() {
        menus.put(AppMenu.MenuName.SIGNUP_MENU, AppMenu.getMenu(AppMenu.MenuName.SIGNUP_MENU, scanner));
        menus.put(AppMenu.MenuName.LOGIN_MENU, AppMenu.getMenu(AppMenu.MenuName.LOGIN_MENU, scanner));
        menus.put(AppMenu.MenuName.Game_Menu, AppMenu.getMenu(AppMenu.MenuName.Game_Menu, scanner));

        currentMenu = menus.get(AppMenu.MenuName.SIGNUP_MENU);
        currentUser = null;
    }

    public void run() {
        while (true) {
            currentMenu.run();
        }
    }
}
