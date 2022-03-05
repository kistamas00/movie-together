package org.kis.movietogether.controller.event;

import org.kis.movietogether.model.websocket.user.User;

import java.util.Set;

public interface WebSocketEventListener extends EventListener {

    void userConnectedToHost(final User user);

    void userDisconnectedFromHost(final User user);

    void connectedToHost(final User host);

    void disconnectedFromHost();

    void userListUpdated(final Set<User> users);
}
