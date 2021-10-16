package com.app.candlesticks.service;

import com.app.candlesticks.entity.Instrument;
import com.app.candlesticks.messaging.event.InstrumentEvent;
import com.app.candlesticks.messaging.event.InstrumentEventType;
import com.app.candlesticks.repo.InstrumentRepository;
import com.app.candlesticks.repo.QuoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.app.candlesticks.messaging.event.InstrumentEventType.ADD;
import static com.app.candlesticks.messaging.event.InstrumentEventType.DELETE;

@Service
@Slf4j
@Data
public class InstrumentEventService implements EventService<InstrumentEvent> {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        InstrumentEvent quoteEvent = mapper.readValue(event, InstrumentEvent.class);
        log.info("instrument event handling");

        InstrumentEventType eventType = quoteEvent.getType();
        Instrument instrument = quoteEvent.getData();

        final String isin = instrument.getIsin();

        if (eventType == ADD) {
            if (instrumentRepository.existsById(isin)) {
                instrumentRepository.deleteByIsin(isin);
            }
            instrumentRepository.save(instrument);
        } else if (eventType == DELETE) {
            quoteRepository.deleteAllByIsin(isin);
            instrumentRepository.deleteByIsin(isin);
        }
    }
}
