package com.app.candlesticks.messaging.service;

import com.app.candlesticks.entity.Instrument;
import com.app.candlesticks.messaging.event.InstrumentEvent;
import com.app.candlesticks.messaging.event.InstrumentEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.app.candlesticks.messaging.event.InstrumentEventType.ADD;
import static com.app.candlesticks.messaging.event.InstrumentEventType.DELETE;

@Service
@Slf4j
@Data
public class InstrumentEventService extends EventService {

    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        InstrumentEvent quoteEvent = mapper.readValue(event, InstrumentEvent.class);
        log.info("instrument event handling");

        InstrumentEventType eventType = quoteEvent.getType();
        Instrument instrument = quoteEvent.getData();

        final String isin = instrument.getIsin();

        if (eventType == ADD) {
            instrumentRepository.save(instrument);
        } else if (eventType == DELETE) {
            quoteRepository.deleteAllByIsin(isin);
            instrumentRepository.deleteByIsin(isin);
        }
    }
}
