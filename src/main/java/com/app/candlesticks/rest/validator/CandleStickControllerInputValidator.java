package com.app.candlesticks.rest.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Component
public class CandleStickControllerInputValidator {


    public void validateTheInput(String isin, Long candlestickLengthInMinutes, String timeFrom, String timeTo) {
        LocalDateTime from;
        LocalDateTime to;
        try{
            from = LocalDateTime.parse(timeFrom);
            to = LocalDateTime.parse(timeTo);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time from or time to cannot be parsed");
        }

        if (isin.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ISIN");
        }
        if (candlestickLengthInMinutes <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid candleStick length");
        }

        if (!from.isBefore(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time range is invalid");
        }

        if (from.plusMinutes(candlestickLengthInMinutes).isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candle length is smaller than the input time range");
        }
    }
}
