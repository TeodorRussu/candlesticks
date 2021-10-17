package com.app.candlesticks.messaging.handler;

import com.app.candlesticks.service.QuoteEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuoteStreamSocketHandler extends StreamSocketHandler {

    @Autowired
    public QuoteStreamSocketHandler(QuoteEventService quoteEventService) {
        super(quoteEventService);
    }
}
