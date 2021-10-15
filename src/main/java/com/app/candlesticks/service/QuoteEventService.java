package com.app.candlesticks.service;

import com.app.candlesticks.db.Quote;
import com.app.candlesticks.messaging.event.QuoteEvent;
import com.app.candlesticks.repo.QuoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QuoteEventService implements EventService<QuoteEvent> {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    QuoteRepository repository;

    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        QuoteEvent quoteEvent = mapper.readValue(event, QuoteEvent.class);
        log.info("instrument event handling");

        Quote quote = quoteEvent.getData();
        quote.setIsin("UH8C52373845");
        repository.save(quote);
    }
}
