package com.app.candlesticks.rest.validator;

import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Component
public class CandleStickControllerInputValidator {

    public static final String INVALID_ISIN_ERROR_MESSAGE = "Invalid ISIN";
    public static final String INVALID_CANDLE_STICK_LENGTH_ERROR_MESSAGE = "Invalid candleStick length";
    public static final String TIME_RANGE_IS_INVALID_ERROR_MESSAGE = "Time range is invalid";
    public static final String CANDLE_STICK_LENGTH_INVALID_ERROR_MESSAGE = "CandleStick length is smaller than the input time range";

    public void validateTheInput(String isin, Long candlestickLengthInMinutes, LocalDateTime timeFrom, LocalDateTime timeTo) {

        if (isin.isBlank()) {
            throw new ValidationException(INVALID_ISIN_ERROR_MESSAGE);
        }
        if (candlestickLengthInMinutes <= 0) {
            throw new ValidationException(INVALID_CANDLE_STICK_LENGTH_ERROR_MESSAGE);
        }

        if (!timeFrom.isBefore(timeTo)) {
            throw new ValidationException(TIME_RANGE_IS_INVALID_ERROR_MESSAGE);
        }

        if (timeFrom.plusMinutes(candlestickLengthInMinutes).isAfter(timeTo)) {
            throw new ValidationException(CANDLE_STICK_LENGTH_INVALID_ERROR_MESSAGE);
        }
    }
}
