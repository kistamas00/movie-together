package org.kis.movietogether.model.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kis.movietogether.controller.event.WebSocketEventListener;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventListenerContainerTest {

    private EventListenerContainer eventListenerContainer;

    @BeforeEach
    void setUp() {
        this.eventListenerContainer = new EventListenerContainer();
    }

    @Test
    void testAddAndGetEventListeners() {
        // GIVEN
        final WebSocketEventListener webSocketEventListener = mock(WebSocketEventListener.class);
        eventListenerContainer.addEventListener(webSocketEventListener);

        // WHEN
        final Set<? extends WebSocketEventListener> webSocketEventListeners =
                eventListenerContainer.getEventListeners(webSocketEventListener.getClass());

        // THEN
        assertThat(webSocketEventListeners)
                .isEqualTo(Set.of(webSocketEventListener));
    }
}