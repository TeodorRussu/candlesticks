package com.app.candlesticks.service;

import com.app.candlesticks.repo.InstrumentRepository;
import com.app.candlesticks.repo.QuoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public abstract class EventService {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    QuoteRepository quoteRepository;

    public abstract void handleEvent(String event) throws JsonProcessingException;
}
