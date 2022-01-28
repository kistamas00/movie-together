package org.kis.movietogether.controller.websocket.handler;

import org.kis.movietogether.controller.websocket.WebSocketController;
import org.kis.movietogether.model.websocket.message.AbstractMessage;
import org.kis.movietogether.model.websocket.message.UserDetailsMessage;
import org.kis.movietogether.model.websocket.message.UserListUpdateMessage;
import org.kis.movietogether.model.websocket.user.User;
import org.kis.movietogether.model.websocket.user.UserContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebSocketHandler.class);
    private static final String WEBSOCKET_URI_PREFIX = "ws://";
    public static final String WEBSOCKET_ENDPOINT = "/movie-together";

    private final ConversionService conversionService;
    protected final WebSocketController webSocketController;
    protected final UserContainer userContainer;

    protected AbstractWebSocketHandler(
            ConversionService conversionService, WebSocketController webSocketController, UserContainer userContainer) {
        this.conversionService = conversionService;
        this.webSocketController = webSocketController;
        this.userContainer = userContainer;
    }

    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable exception) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        LOGGER.error("Transport error: [{}, {}, {}]", sessionId, localAddress, exception);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        final String payload = textMessage.getPayload();
        LOGGER.info("Message received: [{}, {}, {}]", sessionId, localAddress, payload);
        final AbstractMessage message = conversionService.convert(payload, AbstractMessage.class);
        Objects.requireNonNull(message);
        switch (message.getMessageType()) {
            case USER_DETAILS:
                handleUserDetailsMessage(session, convertMessage(message, UserDetailsMessage.class));
                break;
            case USER_LIST:
                handleUserListUpdateMessage(session, convertMessage(message, UserListUpdateMessage.class));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported messageType: " + message.getMessageType());
        }
    }

    protected void handleUserDetailsMessage(
            final WebSocketSession session, final UserDetailsMessage message) {
        final String sessionId = session.getId();
        final Optional<User> optionalUser = userContainer.getUserBy(session);
        final String userName = message.getUserName();
        optionalUser.ifPresentOrElse(
                (User user) -> {
                    user.setUserName(userName);
                    LOGGER.info("User name updated: [{}, {}]", sessionId, userName);
                    webSocketController.userDetailsReceived(user);
                },
                () -> LOGGER.warn("Couldn't find user: [{}, {}]", sessionId, userName));
    }

    protected abstract void handleUserListUpdateMessage(
            final WebSocketSession session, final UserListUpdateMessage message);

    protected void sendMessage(final WebSocketSession session, final AbstractMessage message) {
        final String sessionId = session.getId();
        final InetSocketAddress localAddress = session.getLocalAddress();
        final String payload = conversionService.convert(message, String.class);
        Objects.requireNonNull(payload);
        final TextMessage textMessage = new TextMessage(payload);
        try {
            session.sendMessage(textMessage);
            LOGGER.info("Message sent: [{}, {}, {}]", sessionId, localAddress, payload);
        } catch (IOException e) {
            handleTransportError(session, e);
        }
    }

    protected void sendUserDetailsMessage(final WebSocketSession session) {
        final String currentUserName = userContainer.getCurrentUserName();
        final UserDetailsMessage userDetailsMessage = new UserDetailsMessage().setUserName(currentUserName);
        sendMessage(session, userDetailsMessage);
    }

    protected void sendUserListUpdateMessages() {
        final Set<String> users = userContainer.getUsersWithSession().stream()
                .map(User::getUserName)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
        final UserListUpdateMessage newMessage = new UserListUpdateMessage()
                .setUsers(users);
        userContainer.getUsersWithSession().stream()
                .map(User::getSession)
                .forEach((WebSocketSession session) -> {
                    final String sessionId = session.getId();
                    sendMessage(session, newMessage);
                    LOGGER.info("User list sent to: [{}, {}]", sessionId, users);
                });
    }

    private static <T> T convertMessage(final AbstractMessage message, Class<T> type) {
        return type.cast(message);
    }

    public static WebSocketConnectionManager createClientConnection(
            final AbstractWebSocketHandler handler, final String ip, final String port) {
        return new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                handler,
                WEBSOCKET_URI_PREFIX + ip + ":" + port + WEBSOCKET_ENDPOINT);
    }
}
