package org.kis.movietogether.controller.event;

import org.kis.movietogether.model.websocket.user.User;

public interface WebSocketEventListener {
    void userConnectedToHost(final User user);
    void userDisconnectedFromHost(final User user);
}
