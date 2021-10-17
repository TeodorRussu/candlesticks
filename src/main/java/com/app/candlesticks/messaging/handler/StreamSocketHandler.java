package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.EventService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StreamSocketHandler extends AbstractWebSocketHandler {

    public static final String MESSAGE_HANDLING_EXCEPTION_MESSAGE =
            "Error occurred when handling message {}. Exception: {}. Cause: {}.";

    final EventService service;

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        log.info("received message - " + message.getPayload());
        try {
            service.handleEvent(message.getPayload());
        } catch (Exception exception) {
            log.error(MESSAGE_HANDLING_EXCEPTION_MESSAGE, message.getPayload(), exception.getClass().getSimpleName(), exception.getCause());
        }
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        log.info("established connection - " + session);
    }
}