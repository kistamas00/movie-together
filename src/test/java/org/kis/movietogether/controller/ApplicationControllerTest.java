package org.kis.movietogether.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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
        verify(eventListenerContainer, times(1)).addWebSocketEventListeners(webSocketEventListener);
    }
}