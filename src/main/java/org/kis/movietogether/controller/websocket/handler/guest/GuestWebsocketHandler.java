package org.kis.movietogether.controller.websocket.handler.guest;

import org.kis.movietogether.controller.websocket.WebSocketController;
import org.kis.movietogether.controller.websocket.handler.AbstractWebSocketHandler;
import org.kis.movietogether.model.websocket.message.UserDetailsMessage;
import org.kis.movietogether.model.websocket.message.UserListUpdateMessage;
import org.kis.movietogether.model.websocket.user.User;
import org.kis.movietogether.model.websocket.user.UserContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GuestWebsocketHandler extends AbstractWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestWebsocketHandler.class);

    public GuestWebsocketHandler(
            ConversionService conversionService, WebSocketController webSocketController, UserContainer userContainer) {
        super(conversionService, webSocketController, userContainer);
    }

    @Override
    protected Optional<User> handleUserDetailsMessage(WebSocketSession session, UserDetailsMessage message) {
        final Optional<User> optionalUser = super.handleUserDetailsMessage(session, message);
        optionalUser.ifPresent(webSocketController::connectedToHost);
        return optionalUser;
    }

    @Override
    protected void handleUserListUpdateMessage(WebSocketSession session, UserListUpdateMessage message) {
        final String sessionId = session.getId();
        final Set<User> host = userContainer.getUsersWithSession();
        final Set<User> updatedUsers = message.getUserNames().stream()
                .filter(username -> !username.equals(userContainer.getCurrentUserName()))
                .map(User::new)
                .collect(Collectors.toSet());
        final Set<User> newUsers = new HashSet<>();
        newUsers.addAll(host);
        newUsers.addAll(updatedUsers);
        userContainer.clear();
        userContainer.addUsers(newUsers);
        LOGGER.info("User list updated: [{}, {}]", sessionId, newUsers);
        webSocketController.userListUpdated(userContainer.getUsers());
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        LOGGER.info("Connected to server: [{}, {}]", sessionId, localAddress);
        final User user = new User(session);
        userContainer.addUser(user);
        sendUserDetailsMessage(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        LOGGER.info("Disconnected from server: [{}, {}, {}]", sessionId, localAddress, status);
        userContainer.clear();
        webSocketController.disconnectedFromHost();
        webSocketController.userListUpdated(userContainer.getUsers());
    }
}
