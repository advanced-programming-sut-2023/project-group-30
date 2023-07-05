package network.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import network.common.chat.Chat;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class NetworkComponent {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public NetworkComponent(Socket socket) throws IOException {
        this.socket = socket;

        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    protected NetworkComponent(NetworkComponent networkComponent) {
        this.socket = networkComponent.socket;

        this.dataInputStream = networkComponent.dataInputStream;
        this.dataOutputStream = networkComponent.dataOutputStream;
    }

    public void closeAll() throws IOException {
        socket.close();
        dataInputStream.close();
        dataOutputStream.close();
    }

    public void sendPacket(Packet packet) throws IOException {
        dataOutputStream.writeUTF(packetToJSon(packet));
    }

    public Packet readPacket() {
        try {
            String data = dataInputStream.readUTF();
            Packet packet;

            try {
                packet = new Gson().fromJson(data, Packet.class);
            } catch (JsonSyntaxException jsonSyntaxException) {
                System.out.println("Error: not a packet.");
                return null;
            }

            return packet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String packetToJSon(Packet packet) {
        return new Gson().toJson(packet);
    }

    public String readLine() {
        Packet packet = readPacket();

        if (!packet.command.equals("OneLine") || (packet.arguments.size() != 1)) {
            System.out.println("Error.");
            return "";
        }

        String message = packet.arguments.get("message");
        if (message == null) {
            System.out.println("Error.");
            return "";
        }

        return message;
    }

    public void sendLine(String string) throws IOException {
        HashMap<String, String> arguments = new HashMap<String, String>();
        arguments.put("message", string);

        Packet packet = new Packet("OneLine", arguments);
        sendPacket(packet);
    }

    public void sendCookie(Cookie cookie) throws IOException {
        dataOutputStream.writeUTF(new Gson().toJson(cookie));
    }

    public Cookie readCookie() {
        try {
            String data = dataInputStream.readUTF();
            Cookie cookie;

            try {
                cookie = new Gson().fromJson(data, Cookie.class);
            } catch (JsonSyntaxException jsonSyntaxException) {
                System.out.println("Error: not a cookie.");
                return null;
            }

            return cookie;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChat(Chat chat) throws IOException {
        dataOutputStream.writeUTF(new Gson().toJson(chat));
    }

    public Chat readChat() {
        try {
            String data = dataInputStream.readUTF();
            Chat chat;

            try {
                chat = new Gson().fromJson(data, Chat.class);
            } catch (JsonSyntaxException jsonSyntaxException) {
                System.out.println("Error: not a chat.");
                return null;
            }

            return chat;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
