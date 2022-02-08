package org.kis.movietogether.model.event;

import org.kis.movietogether.controller.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
public class EventListenerContainer {

    private final Map<Class<? extends EventListener>, Set<EventListener>> eventListeners;

    public EventListenerContainer() {
        this.eventListeners = new ConcurrentHashMap<>();
    }

    public <T extends EventListener> void addEventListener(final T eventListener) {
        final Class<? extends EventListener> eventType = eventListener.getClass();
        final Set<EventListener> eventListenersByType =
                Optional.ofNullable(this.eventListeners.get(eventType))
                        .orElseGet(() -> {
                            final CopyOnWriteArraySet<EventListener> listeners = new CopyOnWriteArraySet<>();
                            this.eventListeners.put(eventType, listeners);
                            return listeners;
                        });
        eventListenersByType.add(eventListener);
    }

    public <T extends EventListener> Set<T> getEventListeners(final Class<T> eventType) {
        final Set<EventListener> eventListenersByType =
                this.eventListeners.getOrDefault(eventType, Collections.emptySet());
        return eventListenersByType.stream().map(eventType::cast).collect(Collectors.toUnmodifiableSet());
    }
}
