package org.kis.movietogether.controller.websocket.guest.config;

import org.kis.movietogether.controller.websocket.guest.GuestWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class GuestWebSocketConfig {

    @Autowired
    private GuestWebsocketHandler guestWebsocketHandler;

    //@Bean
    public WebSocketConnectionManager webSocketConnectionManager(final GuestWebsocketHandler guestWebsocketHandler) {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                guestWebsocketHandler,
                "ws://localhost:8080/websocket");


        //manager.setAutoStartup(true);
        manager.start();

        return manager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        webSocketConnectionManager(guestWebsocketHandler);
    }
}
