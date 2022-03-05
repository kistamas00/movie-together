package org.kis.movietogether.controller.websocket.handler.host;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.websocket.WebSocketController;
import org.kis.movietogether.model.websocket.message.UserDetailsMessage;
import org.kis.movietogether.model.websocket.message.UserListUpdateMessage;
import org.kis.movietogether.model.websocket.user.User;
import org.kis.movietogether.model.websocket.user.UserContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HostWebsocketHandlerTest {

    @Mock
    private ConversionService conversionService;
    @Mock
    private WebSocketController webSocketController;
    @Mock
    private UserContainer userContainer;

    @InjectMocks
    private HostWebsocketHandler hostWebsocketHandler;

    @Test
    void testAfterConnectionEstablished() throws IOException {
        // GIVEN
        final WebSocketSession session = mock(WebSocketSession.class);
        final User user = new User(session);
        final UserDetailsMessage message = new UserDetailsMessage().setUserName("Server");
        final TextMessage textMessage = new TextMessage("userDetailsMessage");
        when(userContainer.getCurrentUserName()).thenReturn("Server");
        when(conversionService.convert(message, String.class)).thenReturn("userDetailsMessage");

        // WHEN
        hostWebsocketHandler.afterConnectionEstablished(session);

        // THEN
        verify(userContainer, times(1)).addUser(user);
        verify(session, times(1)).sendMessage(textMessage);
    }

    @Test
    void testAfterConnectionClosed() {
        // GIVEN
        final WebSocketSession session = mock(WebSocketSession.class);
        final User user = new User(session);
        when(userContainer.getUserBy(session)).thenReturn(Optional.of(user));

        // WHEN
        hostWebsocketHandler.afterConnectionClosed(session, new CloseStatus(CloseStatus.NO_STATUS_CODE.getCode()));

        // THEN
        verify(userContainer, times(1)).removeUser(user);
        verify(webSocketController, times(1)).userDisconnectedFromHost(user);
    }

    @Test
    void testHandleUserDetailsMessage() throws IOException {
        // GIVEN
        final WebSocketSession session1 = mock(WebSocketSession.class);
        final WebSocketSession session2 = mock(WebSocketSession.class);
        final User user1 = new User(session1).setUserName("User1");
        final User user2 = new User(session2);
        final User updatedUser2 = new User(session2).setUserName("Client");
        final UserDetailsMessage message = new UserDetailsMessage().setUserName("Client");
        final UserListUpdateMessage userListUpdateMessage =
                new UserListUpdateMessage().setUsers(Set.of("User1", "Client"));
        final TextMessage textMessage = new TextMessage("UserListUpdateMessage");
        when(userContainer.getUserBy(session2)).thenReturn(Optional.of(user2));
        when(userContainer.getUsersWithSession()).thenReturn(Set.of(user1, user2));
        when(userContainer.getUsers()).thenReturn(Set.of(user1, updatedUser2));
        when(conversionService.convert(userListUpdateMessage, String.class)).thenReturn("UserListUpdateMessage");

        // WHEN
        hostWebsocketHandler.handleUserDetailsMessage(session2, message);

        // THEN
        verify(webSocketController, times(1)).userConnectedToHost(updatedUser2);
        verify(webSocketController, times(1)).userListUpdated(Set.of(user1, updatedUser2));
        verify(session1, times(1)).sendMessage(textMessage);
        verify(session2, times(1)).sendMessage(textMessage);
        assertThat(user2).isEqualTo(updatedUser2);
    }

    @Test
    void testHandleUserListUpdateMessage() {
        final WebSocketSession session = mock(WebSocketSession.class);
        final UserListUpdateMessage userListUpdateMessage = new UserListUpdateMessage();
        assertThrows(UnsupportedOperationException.class,
                () -> hostWebsocketHandler.handleUserListUpdateMessage(session, userListUpdateMessage));
    }
}