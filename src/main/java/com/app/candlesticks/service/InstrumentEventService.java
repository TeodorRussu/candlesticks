package com.app.candlesticks.service;

import com.app.candlesticks.messaging.event.InstrumentEvent;
import com.app.candlesticks.messaging.event.QuoteEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstrumentEventService implements EventService<InstrumentEvent> {

    @Autowired
    ObjectMapper mapper;

    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        InstrumentEvent quoteEvent = mapper.readValue(event, InstrumentEvent.class);
        log.info("instrument event handling");
    }
}
