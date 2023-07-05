package network.server;

import network.common.Cookie;
import network.common.NetworkComponent;
import network.common.Packet;
import network.server.controller.MainController;
import network.server.controller.menu_controllers.*;
import network.common.messages.MenuMessages;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SingleConnection extends Thread {
    private final NetworkComponent networkComponent;
    private final Server server;
    private boolean isConnected;

    public SingleConnection(Socket socket, Server server) throws IOException {
        this.networkComponent = new NetworkComponent(socket);

        this.server = server;

        isConnected = true;
    }

    @Override
    public void run() {
        try {
            String message;
            Cookie cookie = new Cookie(-1);

            switch (getConnectionType()) {
                case 0:
                    server.addClient(networkComponent);
                    message = "Client connected.";
                    break;
                default:
                    message = "Undefined type.";
                    break;
            }

            System.out.println(message);
            networkComponent.sendCookie(cookie);

            if (message.equals("Client connected."))
                interactWithClient();
            endConnection();
            System.out.println("Client disconnected.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endConnection() throws IOException {
        networkComponent.closeAll();
        server.disconnectClient(networkComponent);

        isConnected = false;
    }

    private int getConnectionType() throws IOException {
        String clientType = networkComponent.readLine();
        switch (clientType) {
            case "CLIENT":
                return 0;
            default:
                return -1;
        }
    }

    private void interactWithClient() throws IOException {
        while (isConnected) { //communicating with one client
            Cookie authorizationCookie = networkComponent.readCookie();
            int ID = authorizationCookie.ID;
            Packet packet = networkComponent.readPacket();

            String result;
            switch (packet.command) {
                case "userLogin":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    String username = packet.arguments.get("username"), password = packet.arguments.get("password");
                    Boolean stayLoggedIn = packet.arguments.get("stayLoggedIn").equals("true");
                    result = LoginMenuController.userLogin(username, password,
                            stayLoggedIn).toString();

                    networkComponent.sendLine(result);

                    if (result.equals(MenuMessages.USER_LOGGED_IN_SUCCESSFULLY.toString())) {
                        Cookie cookie = new Cookie(Server.getNewID());
                        Session session = new Session(cookie, networkComponent, MainController.getCurrentUser());

                        server.addSession(session);
                        networkComponent.sendCookie(cookie);
                    }
                    break;

                case "userLogOut":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    LoginMenuController.userLogOut();

                    result = "Success";
                    Cookie cookie = new Cookie(-1);
                    if (!server.removeSession(authorizationCookie.ID)) {
                        result = "Failure";
                        System.out.println("Invalid attempt to logout");
                    }

                    networkComponent.sendLine(result);
                    networkComponent.sendCookie(cookie);

                    break;

                case "forgotPassword":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    //todo result = LoginMenuController.forgotPassword(packet.arguments.get("username")).toString();
                    result = "Password recovery is not supported";

                    networkComponent.sendLine(result);
                    break;

                case "newCreateUser":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    SignupMenuController.newCreateUser(packet.arguments.get("username"),
                            packet.arguments.get("password"),
                            packet.arguments.get("email"),
                            packet.arguments.get("nickname"),
                            packet.arguments.get("slogan"),
                            Integer.parseInt(packet.arguments.get("securityQ")),
                            packet.arguments.get("securityA"),
                            packet.arguments.get("avatarURL"));

                    networkComponent.sendLine("Success");
                    break;

                case "validateUserCreation":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = SignupMenuController.validateUserCreation(packet.arguments.get("username"),
                            packet.arguments.get("password"),
                            packet.arguments.get("passwordConfirm"),
                            packet.arguments.get("email")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "generateRandomPassword":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = SignupMenuController.generateRandomPassword();

                    networkComponent.sendLine(result);
                    break;

                case "getRandomSlogan":
                    if (ID != -1) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = SignupMenuController.getRandomSlogan();

                    networkComponent.sendLine(result);
                    break;

                case "getAllSlogans":
                    if (ID != -1) {
                        throw new RuntimeException();
                        //return;
                    }

                    HashMap<String, String> slogans = new HashMap<>();

                    for (String slogan :
                            SignupMenuController.getAllSlogans()) {
                        slogans.put(String.valueOf(slogans.size()), slogan);
                    }
                    Packet resultPacket = new Packet("Slogans", slogans);

                    networkComponent.sendPacket(resultPacket);
                    break;

                case "changeUsername":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.changeUsername(packet.arguments.get("username")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "changeNickname":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.changeNickname(packet.arguments.get("nickname")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "changePassword":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.changePassword(packet.arguments.get("oldPassword"),
                            packet.arguments.get("newPassword")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "changeEmail":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.changeEmail(packet.arguments.get("email")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "changeSlogan":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.changeSlogan(packet.arguments.get("slogan")).toString();

                    networkComponent.sendLine(result);
                    break;

                case "getUsername":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.getUsername();

                    networkComponent.sendLine(result);
                    break;

                case "getNickname":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.getNickname();

                    networkComponent.sendLine(result);
                    break;

                case "getEmail":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.getEmail();

                    networkComponent.sendLine(result);
                    break;

                case "getAvatarURL":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.getAvatarURL();

                    networkComponent.sendLine(result);
                    break;

                case "getSlogan":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ProfileMenuController.getSlogan();

                    networkComponent.sendLine(result);
                    break;

                case "setAvatarURL":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    ProfileMenuController.setAvatarURL(packet.arguments.get("selectedAvatarURL"));

                    networkComponent.sendLine("Success");
                    break;

                case "getPrivateChats":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    networkComponent.sendPacket(new Packet("PrivateChats",
                            ChatMenuController.getPrivateChats()));
                    break;

                case "getRooms":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    networkComponent.sendPacket(new Packet("Rooms",
                            ChatMenuController.getRooms()));
                    break;

                case "getPublicChats":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    networkComponent.sendPacket(new Packet("PublicChats",
                            ChatMenuController.getPublicChats()));
                    break;

                case "getChat":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    networkComponent.sendChat(
                            ChatMenuController.getChat(packet.arguments.get("type"),
                                    packet.arguments.get("name"))
                    );
                    break;

                case "sendChatMessage":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.sendChatMessage(packet.arguments.get("message")) ? "Success" : "Failure";
                    networkComponent.sendLine(result);
                    break;

                case "makePrivateChat":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.makePrivateChat(MainController.getCurrentUser().getUsername(),
                            packet.arguments.get("username"));
                    networkComponent.sendLine(result);
                    break;

                case "makeRoom":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.makeRoom(MainController.getCurrentUser().getUsername(),
                            packet.arguments.get("roomName"));
                    networkComponent.sendLine(result);
                    break;

                case "addMember":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.addMember(packet.arguments.get("username"));
                    networkComponent.sendLine(result);
                    break;

                case "canEditMessage":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.canEditMessage(
                            Integer.parseInt(packet.arguments.get("messageID"))
                    ).toString();
                    networkComponent.sendLine(result);
                    break;

                case "editMessage":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.editMessage(
                            Integer.parseInt(packet.arguments.get("messageID")),
                            packet.arguments.get("newMessage")
                    );
                    networkComponent.sendLine(result);
                    break;

                case "deleteMessage":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    result = ChatMenuController.deleteMessage(
                            Integer.parseInt(packet.arguments.get("messageID"))
                    );
                    networkComponent.sendLine(result);
                    break;

                case "getLeaderboard":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendPacket(new Packet("Error", new HashMap<>()));
                        return;
                    }

                    networkComponent.sendLine("Sending");
                    for (HashMap<String, String> argument :
                            LeaderboardMenuController.getLeaderboard()) {
                        networkComponent.sendPacket(new Packet("One", argument));
                    }
                    networkComponent.sendPacket(new Packet("Done", new HashMap<>()));

                    break;

                case "randomizeUserScores":
                    if (!authorizeUser(authorizationCookie)) {
                        networkComponent.sendLine("Error");
                        return;
                    }

                    LeaderboardMenuController.randomizeUserScores();
                    result = "Success";
                    networkComponent.sendLine(result);
                    break;

                case "Disconnect":
                    isConnected = false;
                    break;
                default:
                    networkComponent.sendLine("Error");
                    break;
            }

        }
    }

    private synchronized boolean authorizeUser(Cookie authorizationCookie) {
        int ID = authorizationCookie.ID;

        if (ID == -1) return false;

        for (Session session :
                server.loggedInUserSessions) {
            if (session.getID().equals(ID)) {
                MainController.setCurrentUser(session.getUser());
                return true;
            }
        }

        return false;
    }

}
