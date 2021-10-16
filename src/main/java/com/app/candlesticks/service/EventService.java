package com.app.candlesticks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public interface EventService<T> {
    void handleEvent(String event) throws JsonProcessingException;
}
