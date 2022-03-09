package org.kis.movietogether.controller.websocket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.application.WebSocketApplicationController;
import org.kis.movietogether.model.websocket.user.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketControllerTest {

    @Mock
    private WebSocketApplicationController applicationController;

    @InjectMocks
    private WebSocketController webSocketController;

    @Test
    void testUserConnectedToHost() {
        // GIVEN
        final User user = new User("TestUser");

        // WHEN
        webSocketController.userConnectedToHost(user);

        // THEN
        verify(applicationController, times(1)).userConnectedToHost(user);
    }

    @Test
    void testUserDisconnectedFromHost() {
        // GIVEN
        final User user = new User("TestUser");

        // WHEN
        webSocketController.userDisconnectedFromHost(user);

        // THEN
        verify(applicationController, times(1)).userDisconnectedFromHost(user);
    }

    @Test
    void testConnectedToHost() {
        // GIVEN
        final User user = new User("TestUser");

        // WHEN
        webSocketController.connectedToHost(user);

        // THEN
        verify(applicationController, times(1)).connectedToHost(user);
    }

    @Test
    void testDisconnectedFromHost() {
        // WHEN
        webSocketController.disconnectedFromHost();

        // THEN
        verify(applicationController, times(1)).disconnectedFromHost();
    }

    @Test
    void testUserListUpdated() {
        // GIVEN
        final Set<User> users = Set.of(new User("TestUser1"), new User("TestUser2"));

        // WHEN
        webSocketController.userListUpdated(users);

        // THEN
        verify(applicationController, times(1)).userListUpdated(users);
    }
}