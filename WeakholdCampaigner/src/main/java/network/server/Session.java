package network.server;

import network.server.model.User;
import network.common.Cookie;
import network.common.NetworkComponent;

public class Session {
    private final Cookie cookie;
    private final NetworkComponent networkComponent;
    private final User user;

    public Session(Cookie cookie, NetworkComponent networkComponent, User user) {
        this.cookie = cookie;
        this.networkComponent = networkComponent;
        this.user = user;
    }

    public Integer getID() {
        return cookie.ID;
    }

    public User getUser() {
        return user;
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }
}
