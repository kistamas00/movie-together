package org.kis.movietogether.controller.application;

import org.kis.movietogether.controller.event.UiEventListener;
import org.kis.movietogether.controller.logger.Loggable;
import org.kis.movietogether.model.event.EventListenerContainer;
import org.kis.movietogether.model.websocket.WebSocketMode;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.function.Consumer;

@Loggable
@Controller
public class UiApplicationController extends ApplicationController implements UiEventListener {

    public UiApplicationController(EventListenerContainer eventListenerContainer) {
        super(eventListenerContainer);
    }

    private void notifyAllEventListeners(Consumer<? super UiEventListener> action) {
        final Set<UiEventListener> eventListeners =
                eventListenerContainer.getEventListeners(UiEventListener.class);
        eventListeners.forEach(action);
    }

    @Override
    public void changeUserName(String userName) {
        notifyAllEventListeners(eventListener -> eventListener.changeUserName(userName));
    }

    @Override
    public void changeUserMode(WebSocketMode mode) {
        notifyAllEventListeners(eventListener -> eventListener.changeUserMode(mode));
    }

    @Override
    public void connectToHost(String ip) {
        notifyAllEventListeners(eventListener -> eventListener.connectToHost(ip));
    }

    @Override
    public void connectToHost(String ip, String port) {
        notifyAllEventListeners(eventListener -> eventListener.connectToHost(ip, port));
    }
}
