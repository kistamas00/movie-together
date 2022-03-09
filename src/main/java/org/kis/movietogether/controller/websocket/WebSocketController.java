package org.kis.movietogether.controller.websocket;

import org.kis.movietogether.controller.application.ApplicationController;
import org.kis.movietogether.controller.application.WebSocketApplicationController;
import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.controller.websocket.handler.AbstractWebSocketHandler;
import org.kis.movietogether.controller.websocket.handler.guest.GuestWebsocketHandler;
import org.kis.movietogether.controller.websocket.handler.host.HostWebsocketHandler;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.kis.movietogether.model.websocket.user.User;
import org.kis.movietogether.model.websocket.user.UserContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;

@Controller
public class WebSocketController implements UiEventListener {

    private final WebSocketApplicationController applicationController;
    private final HostWebsocketHandler hostWebsocketHandler;
    private final GuestWebsocketHandler guestWebsocketHandler;
    private final UserContainer userContainer;
    private WebSocketConnectionManager guestConnectionManager;
    private WebSocketMode webSocketMode;
    @Value("${server.port}")
    private String serverPort;

    public WebSocketController(
            WebSocketApplicationController applicationController,
            @Lazy HostWebsocketHandler hostWebsocketHandler,
            @Lazy GuestWebsocketHandler guestWebsocketHandler,
            UserContainer userContainer) {
        this.applicationController = applicationController;
        this.hostWebsocketHandler = hostWebsocketHandler;
        this.guestWebsocketHandler = guestWebsocketHandler;
        this.userContainer = userContainer;
        this.webSocketMode = WebSocketMode.HOST;
    }

    @PostConstruct
    private void postConstruct() {
        this.applicationController.subscribe(this);
    }

    @Override
    public void changeUserName(String userName) {
        userContainer.updateCurrentUserName(userName);
    }

    @Override
    public void changeUserMode(WebSocketMode mode) {
        this.webSocketMode = mode;
    }

    @Override
    public void connectToHost(final String ip) {
        connectToHost(ip, serverPort);
    }

    @Override
    public void connectToHost(String ip, String port) {
        Objects.requireNonNull(userContainer.getCurrentUserName());
        if (webSocketMode == WebSocketMode.HOST) {
            throw new UnsupportedOperationException("Can't connect to an other server as a host!");
        }
        this.guestConnectionManager = AbstractWebSocketHandler.createClientConnection(
                guestWebsocketHandler, ip, port);
        this.guestConnectionManager.start();
    }

    public void userConnectedToHost(final User user) {
        applicationController.userConnectedToHost(user);
    }

    public void userDisconnectedFromHost(final User user) {
        applicationController.userDisconnectedFromHost(user);
    }

    public void connectedToHost(final User host) {
        applicationController.connectedToHost(host);
    }

    public void disconnectedFromHost() {
        applicationController.disconnectedFromHost();
    }

    public void userListUpdated(final Set<User> users) {
        applicationController.userListUpdated(users);
    }
}
