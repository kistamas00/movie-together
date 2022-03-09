package org.kis.movietogether.controller.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UiApplicationControllerTest {

    @Mock
    private EventListenerContainer eventListenerContainer;

    @InjectMocks
    private UiApplicationController applicationController;

    @Test
    void testChangeUserName() {
        // GIVEN
        final UiEventListener uiEventListener = mock(UiEventListener.class);
        when(eventListenerContainer.getEventListeners(UiEventListener.class))
                .thenReturn(Set.of(uiEventListener));

        // WHEN
        applicationController.changeUserName("User");

        // THEN
        verify(uiEventListener, times(1)).changeUserName("User");
    }

    @Test
    void testChangeUserMode() {
        // GIVEN
        final UiEventListener uiEventListener = mock(UiEventListener.class);
        when(eventListenerContainer.getEventListeners(UiEventListener.class))
                .thenReturn(Set.of(uiEventListener));

        // WHEN
        applicationController.changeUserMode(WebSocketMode.GUEST);

        // THEN
        verify(uiEventListener, times(1)).changeUserMode(WebSocketMode.GUEST);
    }

    @Test
    void testConnectToHost() {
        // GIVEN
        final UiEventListener uiEventListener = mock(UiEventListener.class);
        when(eventListenerContainer.getEventListeners(UiEventListener.class))
                .thenReturn(Set.of(uiEventListener));

        // WHEN
        applicationController.connectToHost("ip");

        // THEN
        verify(uiEventListener, times(1)).connectToHost("ip");
    }

    @Test
    void testConnectToHostWithPort() {
        // GIVEN
        final UiEventListener uiEventListener = mock(UiEventListener.class);
        when(eventListenerContainer.getEventListeners(UiEventListener.class))
                .thenReturn(Set.of(uiEventListener));

        // WHEN
        applicationController.connectToHost("ip", "port");

        // THEN
        verify(uiEventListener, times(1)).connectToHost("ip", "port");
    }
}