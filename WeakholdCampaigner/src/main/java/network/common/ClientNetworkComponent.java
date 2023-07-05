package network.common;

import java.io.IOException;

public class ClientNetworkComponent extends NetworkComponent {
    private Cookie authorizationCookie;

    public ClientNetworkComponent(NetworkComponent networkComponent, Cookie authorizationCookie) throws IOException {
        super(networkComponent);
        this.authorizationCookie = authorizationCookie;
    }

    @Override
    public void sendPacket(Packet packet) {
        try {
            sendCookie(authorizationCookie);
            super.sendPacket(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendLine(String string) {
        try {
            sendCookie(authorizationCookie);
            super.sendLine(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeCookie(Cookie cookie) {
        this.authorizationCookie = cookie;
    }
}
