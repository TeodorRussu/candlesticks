package com.app.candlesticks.config;

import com.app.candlesticks.messaging.handler.InstrumentStreamSocketHandler;
import com.app.candlesticks.messaging.handler.QuoteStreamSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
@Profile("test")
public class SocketClientConfig {

    @Bean
    @Autowired
    @Profile("test")
    WebSocketSession instrumentStream(InstrumentStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketSession webSocketSession =  Mockito.mock(WebSocketSession.class);
        return webSocketSession;
    }

    @Bean
    @Profile("test")
    @Autowired
    WebSocketSession quoteStream(QuoteStreamSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketSession webSocketSession =  Mockito.mock(WebSocketSession.class);
        return webSocketSession;
    }
}
