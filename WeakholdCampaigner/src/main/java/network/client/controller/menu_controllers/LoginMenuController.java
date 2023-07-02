package client.controller.menu_controllers;

import common.Cookie;
import server.Database;
import client.Client;
import common.Packet;
import common.messages.MenuMessages;

import java.util.HashMap;

public class LoginMenuController extends MenuController {
    public static MenuMessages userLogin(String username, String password, Boolean stayLoggedIn) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        arguments.put("stayLoggedIn", stayLoggedIn.toString());
        Client.clientNetworkComponent.sendPacket(new Packet("userLogin", arguments));

        String result = Client.clientNetworkComponent.readLine();

        if (result.equals(MenuMessages.USER_LOGGED_IN_SUCCESSFULLY.toString())) {
            Cookie cookie = Client.clientNetworkComponent.readCookie();
            Client.clientNetworkComponent.changeCookie(cookie);
            System.out.println("successfully received a cookie with id=" + cookie.ID);
        }

        return getMenuMessage(result);
    }

    public static MenuMessages forgotPassword(String username) {
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        Client.clientNetworkComponent.sendPacket(new Packet("forgotPassword", arguments));

        String result = Client.clientNetworkComponent.readLine();

        return getMenuMessage(result);
    }

    public static void userLogOut() {
        Database.saveStayLoggedInUser(null);
    }
}
