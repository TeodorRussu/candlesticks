package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.InstrumentEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
@Slf4j
public class InstrumentStreamSocketHandler extends AbstractWebSocketHandler {

    @Autowired
    InstrumentEventService service;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        log.info("received message - " + message.getPayload());
        service.handleEvent(message.getPayload());

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("established connection - " + session);
    }

}


