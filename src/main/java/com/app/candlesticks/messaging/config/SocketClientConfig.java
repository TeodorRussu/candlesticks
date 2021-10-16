package com.app.candlesticks.messaging.config;

import com.app.candlesticks.messaging.handler.InstrumentStreamSocketHandler;
import com.app.candlesticks.messaging.handler.QuoteStreamSocketHandler;
import com.app.candlesticks.messaging.handler.StreamSocketHandler;
import com.app.candlesticks.service.InstrumentEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
public class SocketClientConfig {

    @Bean
    @Autowired
    WebSocketSession instrumentStream(InstrumentStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        WebSocketSession webSocketSession = webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create("ws://localhost:8080/instruments")).get();
        return webSocketSession;
    }

    @Bean
    @Autowired
    WebSocketSession quoteStream(QuoteStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        WebSocketSession webSocketSession = webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create("ws://localhost:8080/quotes")).get();
        return webSocketSession;
    }
}
