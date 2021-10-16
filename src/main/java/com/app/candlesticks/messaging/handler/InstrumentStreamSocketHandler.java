package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.InstrumentEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentStreamSocketHandler extends StreamSocketHandler {

    @Autowired
    InstrumentEventService service;

    public InstrumentStreamSocketHandler(InstrumentEventService service) {
        super(service);
    }
}


