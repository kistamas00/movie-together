package org.kis.movietogether.controller.websocket.handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractWebSocketHandlerTest {

    @Test
    void testCreateClientConnection() {
        // GIVEN
        final AbstractWebSocketHandler handler = Mockito.mock(AbstractWebSocketHandler.class);

        // WHEN
        final WebSocketConnectionManager webSocketConnectionManager =
                AbstractWebSocketHandler.createClientConnection(handler, "ip", "12345");

        // THEN
        assertThat(webSocketConnectionManager).isNotNull();
    }
}