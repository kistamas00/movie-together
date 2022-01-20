package org.kis.movietogether.controller.websocket.host.config;

import org.kis.movietogether.controller.websocket.host.HostWebsocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class HostWebSocketConfig implements WebSocketConfigurer {

    private final HostWebsocketHandler hostWebsocketHandler;

    public HostWebSocketConfig(HostWebsocketHandler hostWebsocketHandler) {
        this.hostWebsocketHandler = hostWebsocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(hostWebsocketHandler, "/websocket")
                .setAllowedOriginPatterns("*");
    }
}
