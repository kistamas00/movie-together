package org.kis.movietogether.controller;

import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.kis.movietogether.model.websocket.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Controller
public class ApplicationController implements WebSocketEventListener, UiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private final EventListenerContainer eventListenerContainer;

    public ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
    }

    // TODO remove
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws InterruptedException {
        changeUserName("Server");

        /*changeUserMode(WebSocketMode.GUEST);
        changeUserName("TestUser");
        connectToHost("localhost", "29292");*/
    }

    public void subscribe(final WebSocketEventListener eventListener) {
        eventListenerContainer.addEventListener(WebSocketEventListener.class, eventListener);
    }

    public void subscribe(final UiEventListener eventListener) {
        eventListenerContainer.addEventListener(UiEventListener.class, eventListener);
    }

    private static <T> void notifyAllListeners(final Set<T> listeners, final Consumer<? super T> action) {
        listeners.forEach(action);
    }

    private void notifyAllWebSocketEventListeners(final Consumer<? super WebSocketEventListener> action) {
        final Set<WebSocketEventListener> webSocketEventListeners =
                eventListenerContainer.getEventListeners(WebSocketEventListener.class);
        notifyAllListeners(webSocketEventListeners, action);

    }

    private void notifyAllUiEventListeners(final Consumer<? super UiEventListener> action) {
        final Set<UiEventListener> iuUiEventListeners =
                eventListenerContainer.getEventListeners(UiEventListener.class);
        notifyAllListeners(iuUiEventListeners, action);

    }

    @Override
    public void userConnectedToHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userConnectedToHost(user));
        LOGGER.info("userConnectedToHost event has been sent to the WebSocketEventListeners");
    }

    @Override
    public void userDisconnectedFromHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userDisconnectedFromHost(user));
        LOGGER.info("userDisconnectedFromHost event has been sent to the WebSocketEventListeners");
    }

    @Override
    public void connectedToHost(final User host) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.connectedToHost(host));
        LOGGER.info("userConnectedToHost event has been sent to the WebSocketEventListeners");
    }

    @Override
    public void disconnectedFromHost() {
        notifyAllWebSocketEventListeners(WebSocketEventListener::disconnectedFromHost);
        LOGGER.info("disconnectedFromHost event has been sent to the WebSocketEventListeners");
    }

    @Override
    public void userListUpdated(final Set<User> users) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userListUpdated(users));
        LOGGER.info("userListUpdated event has been sent to the WebSocketEventListeners");
    }

    @Override
    public void changeUserName(String userName) {
        notifyAllUiEventListeners(eventListener -> eventListener.changeUserName(userName));
        LOGGER.info("changeUserName event has been sent to the UiEventListeners");
    }

    @Override
    public void changeUserMode(WebSocketMode mode) {
        notifyAllUiEventListeners(eventListener -> eventListener.changeUserMode(mode));
        LOGGER.info("changeUserMode event has been sent to the UiEventListeners");
    }

    @Override
    public void connectToHost(String ip) {
        notifyAllUiEventListeners(eventListener -> eventListener.connectToHost(ip));
        LOGGER.info("connectToHost event has been sent to the UiEventListeners");
    }

    @Override
    public void connectToHost(String ip, String port) {
        notifyAllUiEventListeners(eventListener -> eventListener.connectToHost(ip, port));
        LOGGER.info("connectToHost event has been sent to the UiEventListeners");
    }
}
