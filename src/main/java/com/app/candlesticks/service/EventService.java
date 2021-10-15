package com.app.candlesticks.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventService<T> {
    void handleEvent(String event) throws JsonProcessingException;
}
