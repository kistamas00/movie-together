package org.kis.movietogether.model.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EventListenerContainerTest {

    private EventListenerContainer eventListenerContainer;

    @BeforeEach
    void setUp() {
        this.eventListenerContainer = new EventListenerContainer();
    }

    @Test
    void testAddMediaEventListener() {
        // GIVEN
        final MediaEventListener mediaEventListener = mock(MediaEventListener.class);
        eventListenerContainer.addMediaEventListener(mediaEventListener);

        // WHEN
        final Set<MediaEventListener> mediaEventListeners = eventListenerContainer.getMediaEventListeners();

        // THEN
        assertThat(mediaEventListeners)
                .containsExactly(mediaEventListener);
    }

    @Test
    void testAddWebSocketEventListeners() {
         // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        eventListenerContainer.addWebSocketEventListeners(webSocketEventListener);

        // WHEN
        final Set<WebSocketEventListener> webSocketEventListeners = eventListenerContainer.getWebSocketEventListeners();

        // THEN
        assertThat(webSocketEventListeners)
                .containsExactly(webSocketEventListener);
    }

    @Test
    void testGetMediaEventListeners() {
        // GIVEN
        final MediaEventListener mediaEventListener = mock(MediaEventListener.class);
        eventListenerContainer.addMediaEventListener(mediaEventListener);

        // WHEN
        final Set<MediaEventListener> mediaEventListeners = eventListenerContainer.getMediaEventListeners();

        // THEN
        assertThrows(UnsupportedOperationException.class, () -> mediaEventListeners.remove(mediaEventListener));
    }

    @Test
    void testGetWebSocketEventListeners() {
         // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        eventListenerContainer.addWebSocketEventListeners(webSocketEventListener);

        // WHEN
        final Set<WebSocketEventListener> webSocketEventListeners = eventListenerContainer.getWebSocketEventListeners();

        // THEN
        assertThrows(UnsupportedOperationException.class, () -> webSocketEventListeners.remove(webSocketEventListener));
    }
}