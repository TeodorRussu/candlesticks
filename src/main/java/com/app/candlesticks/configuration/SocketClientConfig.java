package com.app.candlesticks.configuration;

import com.app.candlesticks.messaging.handler.InstrumentStreamSocketHandler;
import com.app.candlesticks.messaging.handler.QuoteStreamSocketHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "websockets")
public class SocketClientConfig {

    private String quotesWebsocketUri;
    private String instrumentsWebsocketUri;

    @Bean
    @Autowired
    WebSocketSession instrumentStream(InstrumentStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        return webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create(instrumentsWebsocketUri)).get();
    }

    @Bean
    @Autowired
    WebSocketSession quoteStream(QuoteStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        return webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create(quotesWebsocketUri)).get();
    }
}
