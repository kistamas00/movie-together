package org.kis.movietogether.controller.event;

import org.kis.movietogether.model.websocket.WebSocketMode;

public interface UiEventListener extends EventListener {

    void changeUserName(final String userName);

    void changeUserMode(final WebSocketMode mode);

    void connectToHost(final String ip);

    void connectToHost(final String ip, final String port);
}
