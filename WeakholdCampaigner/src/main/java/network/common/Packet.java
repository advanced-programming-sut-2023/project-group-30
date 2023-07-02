package network.common;

import java.util.HashMap;

public class Packet {
    public final String command;
    public final HashMap<String, String> arguments;

    public Packet(String command, HashMap<String, String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }
}
