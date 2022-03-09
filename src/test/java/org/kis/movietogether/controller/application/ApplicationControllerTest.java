package org.kis.movietogether.controller.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    @Mock
    private EventListenerContainer eventListenerContainer;

    @InjectMocks
    private ApplicationController applicationController;

    @Test
    void testSubscribeWebSocketEventListener() {
        // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);

        // WHEN
        applicationController.subscribe(webSocketEventListener);

        // THEN
        verify(eventListenerContainer, times(1))
                .addEventListener(WebSocketEventListener.class, webSocketEventListener);
    }

    @Test
    void testSubscribeUiEventListener() {
        // GIVEN
        final UiEventListener uiEventListener = mock(UiEventListener.class);

        // WHEN
        applicationController.subscribe(uiEventListener);

        // THEN
        verify(eventListenerContainer, times(1))
                .addEventListener(UiEventListener.class, uiEventListener);
    }
}