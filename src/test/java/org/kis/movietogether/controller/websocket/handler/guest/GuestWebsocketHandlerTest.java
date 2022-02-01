package org.kis.movietogether.controller.websocket.handler.guest;

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
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestWebsocketHandlerTest {

    @Mock
    private ConversionService conversionService;
    @Mock
    private WebSocketController webSocketController;
    @Mock
    private UserContainer userContainer;

    @InjectMocks
    private GuestWebsocketHandler guestWebsocketHandler;

    @Test
    void testAfterConnectionEstablished() throws IOException {
        // GIVEN
        final WebSocketSession session = mock(WebSocketSession.class);
        final User host = new User(session);
        final UserDetailsMessage message = new UserDetailsMessage().setUserName("Client");
        final TextMessage textMessage = new TextMessage("userDetailsMessage");
        when(userContainer.getCurrentUserName()).thenReturn("Client");
        when(conversionService.convert(message, String.class)).thenReturn("userDetailsMessage");

        // WHEN
        guestWebsocketHandler.afterConnectionEstablished(session);

        // THEN
        verify(userContainer, times(1)).addUser(host);
        verify(webSocketController, times(1)).connectedToHost(host);
        verify(session, times(1)).sendMessage(textMessage);
    }

    @Test
    void testAfterConnectionClosed() {
        // GIVEN
        final WebSocketSession session = mock(WebSocketSession.class);

        // WHEN
        guestWebsocketHandler.afterConnectionClosed(session, new CloseStatus(CloseStatus.NO_STATUS_CODE.getCode()));

        // THEN
        verify(userContainer, times(1)).clear();
        verify(webSocketController, times(1)).disconnectedFromHost();
    }

    @Test
    void testHandleUserListUpdateMessage() {
        // GIVEN
        final WebSocketSession session = mock(WebSocketSession.class);
        final UserListUpdateMessage userListUpdateMessage =
                new UserListUpdateMessage().setUsers(Set.of("User1", "User2"));
        final User host = new User(session);
        final Set<User> updatedUsers = Set.of(host, new User("User1"), new User("User2"));
        when(userContainer.getUsersWithSession()).thenReturn(Set.of(host));
        when(userContainer.getUsers()).thenReturn(updatedUsers);

        // WHEN
        guestWebsocketHandler.handleUserListUpdateMessage(session, userListUpdateMessage);

        // THEN
        verify(userContainer, times(1)).clear();
        verify(userContainer, times(1)).addUsers(updatedUsers);
        verify(webSocketController, times(1)).userListUpdated(updatedUsers);
    }
}