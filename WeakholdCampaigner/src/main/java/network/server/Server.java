package network.server;

import network.server.controller.MainController;
import network.common.NetworkComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ArrayList<NetworkComponent> clientsConnectedToSocket = new ArrayList<>();
    public final ArrayList<Session> loggedInUserSessions = new ArrayList<>();

    public void run() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(1234);

        Thread connectionAcceptingThread = new Thread(() -> {

            while (true) {

                try {
                    Socket socket = serverSocket.accept(); //wait for a connection attempt

                    Thread singleConnection = new SingleConnection(socket, this);
                    singleConnection.setDaemon(true);

                    singleConnection.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });
        connectionAcceptingThread.setDaemon(true);
        connectionAcceptingThread.start();

        runApp();

        System.out.println("Server started up successfully. Type 'exit' to end it safely.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.nextLine().equals("exit")) {
                break;
            }
        }

        Database.saveAllUsers();
    }

    public synchronized void addClient(NetworkComponent networkComponent) {
        clientsConnectedToSocket.add(networkComponent);
    }

    private static void runApp() {
        MainController mainController = new MainController();
        mainController.initializeApp();
    }

    private static int previousID = 999;
    public static int getNewID() {
        return ++previousID;
    }

    public void addSession(Session session) {
        this.loggedInUserSessions.add(session);
    }

    public boolean removeSession(Integer ID) {
        Session toRemove = null;
        for (Session session :
                this.loggedInUserSessions) {
            if (session.getID().equals(ID)) {
                toRemove = session;
                break;
            }
        }

        if (toRemove == null) return false;

        this.loggedInUserSessions.remove(toRemove);
        return true;
    }

    public void disconnectClient(NetworkComponent networkComponent) {
        if (this.clientsConnectedToSocket.remove(networkComponent)) {
            Session toRemove = null;
            for (Session session :
                    this.loggedInUserSessions) {
                if (session.getNetworkComponent().equals(networkComponent)) {
                    toRemove = session;
                    break;
                }
            }

            this.loggedInUserSessions.remove(toRemove);
            if (toRemove != null) //toRemove will be null if a client closes while no user is logged in on it
                toRemove.getUser().toggleLastSeen();
        }
    }
}
