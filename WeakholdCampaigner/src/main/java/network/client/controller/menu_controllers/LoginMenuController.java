package network.client.controller.menu_controllers;

import network.client.Client;
import network.common.Cookie;
import network.common.Packet;
import network.common.messages.MenuMessages;

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

        //todo return getMenuMessage(result);
        System.out.println(result);
        return MenuMessages.STAY;
    }

    public static void userLogOut() {
        Client.clientNetworkComponent.sendPacket(new Packet("userLogOut", new HashMap<>()));

        String result = Client.clientNetworkComponent.readLine();

        if (!result.equals("Success"))
            throw new RuntimeException();

        Cookie cookie = Client.clientNetworkComponent.readCookie();
        Client.clientNetworkComponent.changeCookie(cookie);
        System.out.println("successfully received a (logout) cookie with id=" + cookie.ID);
    }
}
