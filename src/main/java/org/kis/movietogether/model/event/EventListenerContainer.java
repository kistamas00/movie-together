package org.kis.movietogether.model.event;

import org.kis.movietogether.controller.event.MediaEventListener;
import org.kis.movietogether.controller.event.WebSocketEventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class EventListenerContainer {

    private final Set<MediaEventListener> mediaEventListeners;
    private final Set<WebSocketEventListener> webSocketEventListeners;

    public EventListenerContainer() {
        this.mediaEventListeners = new CopyOnWriteArraySet<>();
        this.webSocketEventListeners = new CopyOnWriteArraySet<>();
    }

    public void addMediaEventListener(final MediaEventListener mediaEventListener) {
        mediaEventListeners.add(mediaEventListener);
    }

    public void addWebSocketEventListeners(final WebSocketEventListener webSocketEventListener) {
        webSocketEventListeners.add(webSocketEventListener);
    }

    public Set<MediaEventListener> getMediaEventListeners() {
        return Collections.unmodifiableSet(mediaEventListeners);
    }

    public Set<WebSocketEventListener> getWebSocketEventListeners() {
        return Collections.unmodifiableSet(webSocketEventListeners);
    }
}
