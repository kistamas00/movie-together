package org.kis.movietogether.controller;

import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.websocket.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Controller
public class ApplicationController implements WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private final EventListenerContainer eventListenerContainer;

    public ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
    }

    public void subscribe(final WebSocketEventListener eventListener) {
        eventListenerContainer.addEventListener(eventListener);
    }

    private static <T> void notifyAllListeners(final Set<T> listeners, final Consumer<? super T> action) {
        listeners.forEach(action);
    }

    private void notifyAllWebSocketEventListeners(final Consumer<? super WebSocketEventListener> action) {
        final Set<WebSocketEventListener> webSocketEventListeners =
                eventListenerContainer.getEventListeners(WebSocketEventListener.class);
        notifyAllListeners(webSocketEventListeners, action);

    }

    @Override
    public void userConnectedToHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userConnectedToHost(user));
        LOGGER.info("userConnectedToHost event has been sent to the listeners");
    }

    @Override
    public void userDisconnectedFromHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userDisconnectedFromHost(user));
        LOGGER.info("userDisconnectedFromHost event has been sent to the listeners");
    }

    @Override
    public void connectedToHost(final User host) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.connectedToHost(host));
        LOGGER.info("userConnectedToHost event has been sent to the listeners");
    }

    @Override
    public void disconnectedFromHost() {
        notifyAllWebSocketEventListeners(WebSocketEventListener::disconnectedFromHost);
        LOGGER.info("disconnectedFromHost event has been sent to the listeners");
    }

    @Override
    public void userListUpdated(final Set<User> users) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userListUpdated(users));
        LOGGER.info("userListUpdated event has been sent to the listeners");
    }
}
