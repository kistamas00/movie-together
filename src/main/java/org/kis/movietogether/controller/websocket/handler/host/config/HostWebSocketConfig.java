package org.kis.movietogether.controller.websocket.handler.host.config;

import org.kis.movietogether.controller.websocket.handler.host.HostWebsocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static org.kis.movietogether.controller.websocket.handler.AbstractWebSocketHandler.WEBSOCKET_ENDPOINT;

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
                .addHandler(hostWebsocketHandler, WEBSOCKET_ENDPOINT)
                .setAllowedOriginPatterns("*");
    }
}
