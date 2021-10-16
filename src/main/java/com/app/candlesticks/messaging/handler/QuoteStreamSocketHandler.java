package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.EventService;
import com.app.candlesticks.service.InstrumentEventService;
import com.app.candlesticks.service.QuoteEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@Component
public class QuoteStreamSocketHandler extends StreamSocketHandler {

    @Autowired
    QuoteEventService service;

    public QuoteStreamSocketHandler(QuoteEventService service) {
        super(service);
    }
}
