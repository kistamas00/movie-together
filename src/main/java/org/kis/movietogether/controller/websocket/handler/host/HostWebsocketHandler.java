package org.kis.movietogether.controller.websocket.handler.host;

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

@Component
public class HostWebsocketHandler extends AbstractWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostWebsocketHandler.class);

    public HostWebsocketHandler(
            ConversionService conversionService, WebSocketController webSocketController, UserContainer userContainer) {
        super(conversionService, webSocketController, userContainer);
    }

    @Override
    protected void handleUserDetailsMessage(WebSocketSession session, UserDetailsMessage message) {
        super.handleUserDetailsMessage(session, message);
        sendUserListUpdateMessages();
    }

    @Override
    protected void handleUserListUpdateMessage(WebSocketSession session, UserListUpdateMessage message) {
        throw new UnsupportedOperationException("Unsupported user list update message from the client!");
    }


    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        LOGGER.info("Client connected to server: [{}, {}]", sessionId, localAddress);
        final User user = new User(session);
        userContainer.addUser(user);
        webSocketController.userConnectedToHost(user);
        sendUserDetailsMessage(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        LOGGER.info("Client disconnected from the server: [{}, {}, {}]", sessionId, localAddress, status);
        userContainer.getUserBy(session).ifPresent((User user) -> {
            userContainer.removeUser(user);
            webSocketController.userDisconnectedFromHost(user);
        });
    }
}
