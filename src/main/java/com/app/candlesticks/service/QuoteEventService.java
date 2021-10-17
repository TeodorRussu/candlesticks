package com.app.candlesticks.service;

import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.messaging.event.QuoteEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class QuoteEventService extends EventService {

    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        QuoteEvent quoteEvent = mapper.readValue(event, QuoteEvent.class);
        log.info("instrument event handling");

        Quote quote = quoteEvent.getData();
        if (instrumentRepository.existsById(quote.getIsin()))
            quoteRepository.save(quote);
    }
}
