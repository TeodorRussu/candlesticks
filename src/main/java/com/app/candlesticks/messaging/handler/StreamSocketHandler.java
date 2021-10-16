package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Data
@RequiredArgsConstructor
public class StreamSocketHandler extends AbstractWebSocketHandler {

    public static final String MESSAGE_HANDLING_EXCEPTION_MESSAGE =
            "Error occurred when handling message {0}. Exception: {1}. Cause: {2}.";

    final EventService service;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        log.info("received message - " + message.getPayload());
        try {
            service.handleEvent(message.getPayload());
        } catch (Exception exception) {
            log.error(MESSAGE_HANDLING_EXCEPTION_MESSAGE, message.getPayload(), exception.getClass().getSimpleName(), exception.getCause());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("established connection - " + session);
    }
}