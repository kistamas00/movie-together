package org.kis.movietogether.controller.application;

import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.controller.logger.Loggable;
import org.kis.movietogether.model.event.EventListenerContainer;

@Loggable
public class ApplicationController {

    protected final EventListenerContainer eventListenerContainer;

    protected ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
    }

    public void subscribe(final WebSocketEventListener eventListener) {
        eventListenerContainer.addEventListener(WebSocketEventListener.class, eventListener);
    }

    public void subscribe(final UiEventListener eventListener) {
        eventListenerContainer.addEventListener(UiEventListener.class, eventListener);
    }
}
