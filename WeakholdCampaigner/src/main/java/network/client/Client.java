package network.client;

import network.common.ClientNetworkComponent;
import network.common.Cookie;
import network.common.NetworkComponent;
import network.client.GUI.LoginMenu;
import network.common.Packet;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

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
            clientNetworkComponent.sendPacket(new Packet("Disconnect", new HashMap<>()));
            try {
                if (networkComponent != null)
                    networkComponent.closeAll();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Process finished.");
        }
    }

    private static void startApp(String[] args) {
        //mainController.run();
        LoginMenu.main(args);
    }
}
