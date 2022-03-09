package org.kis.movietogether.controller.application;

import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.kis.movietogether.controller.logger.Loggable;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.model.websocket.user.User;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Loggable
@Controller
public class WebSocketApplicationController extends ApplicationController implements WebSocketEventListener {

    public WebSocketApplicationController(EventListenerContainer eventListenerContainer) {
        super(eventListenerContainer);
    }

    private void notifyAllEventListeners(Consumer<? super WebSocketEventListener> action) {
        final Set<WebSocketEventListener> eventListeners =
                eventListenerContainer.getEventListeners(WebSocketEventListener.class);
        eventListeners.forEach(action);
    }

    @Override
    public void userConnectedToHost(final User user) {
        notifyAllEventListeners(eventListener -> eventListener.userConnectedToHost(user));
    }

    @Override
    public void userDisconnectedFromHost(final User user) {
        notifyAllEventListeners(eventListener -> eventListener.userDisconnectedFromHost(user));
    }

    @Override
    public void connectedToHost(final User host) {
        notifyAllEventListeners(eventListener -> eventListener.connectedToHost(host));
    }

    @Override
    public void disconnectedFromHost() {
        notifyAllEventListeners(WebSocketEventListener::disconnectedFromHost);
    }

    @Override
    public void userListUpdated(final Set<User> users) {
        notifyAllEventListeners(eventListener -> eventListener.userListUpdated(users));
    }
}
