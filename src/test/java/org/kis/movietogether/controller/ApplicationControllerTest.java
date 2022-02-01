package org.kis.movietogether.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.model.websocket.user.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    @Mock
    private EventListenerContainer eventListenerContainer;

    @InjectMocks
    private ApplicationController applicationController;

    @Test
    void testSubscribeMediaEventListener() {
        // GIVEN
        final MediaEventListener mediaEventListener = mock(MediaEventListener.class);

        // WHEN
        applicationController.subscribe(mediaEventListener);

        // THEN
        verify(eventListenerContainer, times(1)).addMediaEventListener(mediaEventListener);
    }

    @Test
    void testSubscribeWebSocketEventListener() {
        // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);

        // WHEN
        applicationController.subscribe(webSocketEventListener);

        // THEN
        verify(eventListenerContainer, times(1)).addWebSocketEventListener(webSocketEventListener);
    }

    @Test
    void testUserConnectedToHost() {
        // GIVEN
        final User user = new User("TestUser");
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        when(eventListenerContainer.getWebSocketEventListeners()).thenReturn(Set.of(webSocketEventListener));

        // WHEN
        applicationController.userConnectedToHost(user);

        // THEN
        verify(webSocketEventListener, times(1)).userConnectedToHost(user);
    }

    @Test
    void testUserDisconnectedFromHost() {
        // GIVEN
        final User user = new User("TestUser");
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        when(eventListenerContainer.getWebSocketEventListeners()).thenReturn(Set.of(webSocketEventListener));

        // WHEN
        applicationController.userDisconnectedFromHost(user);

        // THEN
        verify(webSocketEventListener, times(1)).userDisconnectedFromHost(user);
    }

    @Test
    void testConnectedToHost() {
        // GIVEN
        final User user = new User("TestUser");
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        when(eventListenerContainer.getWebSocketEventListeners()).thenReturn(Set.of(webSocketEventListener));

        // WHEN
        applicationController.connectedToHost(user);

        // THEN
        verify(webSocketEventListener, times(1)).connectedToHost(user);
    }

    @Test
    void testDisconnectedFromHost() {
        // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        when(eventListenerContainer.getWebSocketEventListeners()).thenReturn(Set.of(webSocketEventListener));

        // WHEN
        applicationController.disconnectedFromHost();

        // THEN
        verify(webSocketEventListener, times(1)).disconnectedFromHost();
    }

    @Test
    void testUserListUpdated() {
        // GIVEN
        final Set<User> users = Set.of(new User("TestUser"));
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        when(eventListenerContainer.getWebSocketEventListeners()).thenReturn(Set.of(webSocketEventListener));

        // WHEN
        applicationController.userListUpdated(users);

        // THEN
        verify(webSocketEventListener, times(1)).userListUpdated(users);
    }
}