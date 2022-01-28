package org.kis.movietogether.controller;

import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.springframework.stereotype.Controller;

@Controller
public final class ApplicationController {

    private final EventListenerContainer eventListenerContainer;

    public ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
    }

    public void subscribe(final MediaEventListener eventListener) {
        eventListenerContainer.addMediaEventListener(eventListener);
    }
    public void subscribe(final WebSocketEventListener eventListener) {
        eventListenerContainer.addWebSocketEventListeners(eventListener);
    }
}
