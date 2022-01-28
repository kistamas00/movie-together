package org.kis.movietogether.controller.websocket;

import org.kis.movietogether.controller.ApplicationController;
import org.kis.movietogether.controller.websocket.handler.AbstractWebSocketHandler;
import org.kis.movietogether.controller.websocket.handler.guest.GuestWebsocketHandler;
import org.kis.movietogether.controller.websocket.handler.host.HostWebsocketHandler;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.kis.movietogether.model.websocket.user.User;
import org.kis.movietogether.model.websocket.user.UserContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.util.Objects;
import java.util.Set;

@Controller
public class WebSocketController {

    private final ApplicationController applicationController;
    private final HostWebsocketHandler hostWebsocketHandler;
    private final GuestWebsocketHandler guestWebsocketHandler;
    private final UserContainer userContainer;
    private WebSocketConnectionManager guestConnectionManager;
    private WebSocketMode webSocketMode;
    @Value("${server.port}")
    private String serverPort;

    public WebSocketController(
            ApplicationController applicationController,
            @Lazy HostWebsocketHandler hostWebsocketHandler,
            @Lazy GuestWebsocketHandler guestWebsocketHandler,
            UserContainer userContainer) {
        this.applicationController = applicationController;
        this.hostWebsocketHandler = hostWebsocketHandler;
        this.guestWebsocketHandler = guestWebsocketHandler;
        this.userContainer = userContainer;
        this.webSocketMode = WebSocketMode.HOST;
    }

    // TODO refactor
    private void changeMode(final WebSocketMode webSocketMode) {
        this.webSocketMode = webSocketMode;
    }

    // TODO refactor
    private void connectToHost(final String ip) {
        Objects.requireNonNull(userContainer.getCurrentUserName());
        if (webSocketMode == WebSocketMode.HOST) {
            throw new UnsupportedOperationException("Can't connect to an other server as a host!");
        }
        this.guestConnectionManager = AbstractWebSocketHandler.createClientConnection(
                guestWebsocketHandler, ip, serverPort);
        this.guestConnectionManager.start();
    }

    // TODO refactor
    private void updateCurrentUserName(final String userName) {
        userContainer.updateCurrentUserName(userName);
    }

    public void userConnectedToHost(final User user) {
    }

    public void userDisconnectedFromHost(final User user) {
    }

    public void userDetailsReceived(final User user) {
    }

    public void connectedToHost(final User host) {
    }

    public void disconnectedFromHost() {
    }

    public void userListReceivedByGuest(final Set<User> user) {
    }

    // TODO remove
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        updateCurrentUserName("Szerver");

        /*serverPort = "29292";
        changeMode(WebSocketMode.GUEST);
        updateCurrentUserName("Tamas");
        connectToHost("localhost");*/
    }
}
