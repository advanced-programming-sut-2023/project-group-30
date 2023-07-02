package client;

import common.Cookie;
import common.ClientNetworkComponent;
import common.NetworkComponent;
import client.GUI.LoginMenu;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static ClientNetworkComponent clientNetworkComponent = null;

    public static void main(String[] args) {

        Cookie authorizationCookie;
        NetworkComponent networkComponent = null;

        Socket socket = null;

        try {
            socket = new Socket("localhost", 1234);

            networkComponent = new NetworkComponent(socket);

            networkComponent.sendLine("CLIENT");
            authorizationCookie = networkComponent.readCookie();
            if (authorizationCookie.ID != -1) {
                System.out.println("Connection failed.");
                return;
            } else System.out.println("connected");

            clientNetworkComponent = new ClientNetworkComponent(networkComponent, authorizationCookie);

            startApp(args);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (networkComponent != null)
                    networkComponent.closeAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void startApp(String[] args) {
        //mainController.run();
        LoginMenu.main(args);
    }
}
