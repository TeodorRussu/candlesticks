package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.messaging.service.InstrumentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentStreamSocketHandler extends StreamSocketHandler {

    @Autowired
    public InstrumentStreamSocketHandler(InstrumentEventService instrumentEventService) {
        super(instrumentEventService);
    }
}


