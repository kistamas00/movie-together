package org.kis.movietogether.controller;

import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.controller.logger.Loggable;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.kis.movietogether.model.websocket.user.User;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Loggable
@Controller
public class ApplicationController implements WebSocketEventListener, UiEventListener {

    private final EventListenerContainer eventListenerContainer;

    public ApplicationController(final EventListenerContainer eventListenerContainer) {
        this.eventListenerContainer = eventListenerContainer;
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
    }

    @Override
    public void userDisconnectedFromHost(final User user) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userDisconnectedFromHost(user));
    }

    @Override
    public void connectedToHost(final User host) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.connectedToHost(host));
    }

    @Override
    public void disconnectedFromHost() {
        notifyAllWebSocketEventListeners(WebSocketEventListener::disconnectedFromHost);
    }

    @Override
    public void userListUpdated(final Set<User> users) {
        notifyAllWebSocketEventListeners(eventListener -> eventListener.userListUpdated(users));
    }

    @Override
    public void changeUserName(String userName) {
        notifyAllUiEventListeners(eventListener -> eventListener.changeUserName(userName));
    }

    @Override
    public void changeUserMode(WebSocketMode mode) {
        notifyAllUiEventListeners(eventListener -> eventListener.changeUserMode(mode));
    }

    @Override
    public void connectToHost(String ip) {
        notifyAllUiEventListeners(eventListener -> eventListener.connectToHost(ip));
    }

    @Override
    public void connectToHost(String ip, String port) {
        notifyAllUiEventListeners(eventListener -> eventListener.connectToHost(ip, port));
    }
}
