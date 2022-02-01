package org.kis.movietogether.controller;

import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.websocket.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Controller
public class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private final EventListenerContainer eventListenerContainer;

    public ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
    }

    public void subscribe(final MediaEventListener eventListener) {
        eventListenerContainer.addMediaEventListener(eventListener);
    }

    public void subscribe(final WebSocketEventListener eventListener) {
        eventListenerContainer.addWebSocketEventListener(eventListener);
    }

    private void notifyAllMediaEventListeners(final Consumer<? super MediaEventListener> action) {
        final Set<MediaEventListener> mediaEventListeners = eventListenerContainer.getMediaEventListeners();
        notifyAllListeners(mediaEventListeners, action);
    }

    private void notifyAllWebSocketEventListeners(final Consumer<? super WebSocketEventListener> action) {
        final Set<WebSocketEventListener> webSocketEventListeners = eventListenerContainer.getWebSocketEventListeners();
        notifyAllListeners(webSocketEventListeners, action);

    }

    private static <T> void notifyAllListeners(final Set<T> listeners, final Consumer<? super T> action) {
        listeners.forEach(action);
    }

    public void userConnectedToHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userConnectedToHost(user));
        LOGGER.info("userConnectedToHost event has been sent to the listeners");
    }

    public void userDisconnectedFromHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userDisconnectedFromHost(user));
        LOGGER.info("userDisconnectedFromHost event has been sent to the listeners");
    }

    public void connectedToHost(final User host) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.connectedToHost(host));
        LOGGER.info("userConnectedToHost event has been sent to the listeners");
    }

    public void disconnectedFromHost() {
        notifyAllWebSocketEventListeners(WebSocketEventListener::disconnectedFromHost);
        LOGGER.info("disconnectedFromHost event has been sent to the listeners");
    }

    public void userListUpdated(final Set<User> users) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userListUpdated(users));
        LOGGER.info("userListUpdated event has been sent to the listeners");
    }
}
