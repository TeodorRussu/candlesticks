package com.app.candlesticks.rest.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Component
public class CandleStickControllerInputValidator {

    public void validateTheInput(String isin, Long candlestickLengthInMinutes, String timeFrom, String timeTo) {
        if (isin.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ISIN");
        }
        if (candlestickLengthInMinutes <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid candleStick length");
        }

        if (!LocalDateTime.parse(timeFrom).isBefore(LocalDateTime.parse(timeTo))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time range is invalid");
        }

        if (LocalDateTime.parse(timeFrom).plusMinutes(candlestickLengthInMinutes).isAfter(LocalDateTime.parse(timeTo))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candle length is smaller than the input time range");
        }
    }
}
