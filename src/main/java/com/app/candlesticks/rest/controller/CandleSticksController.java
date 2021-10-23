package com.app.candlesticks.rest.controller;

import com.app.candlesticks.rest.dto.CandleStick;
import com.app.candlesticks.rest.service.CandleSticksService;
import com.app.candlesticks.rest.validator.CandleStickControllerInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class CandleSticksController {

    public static final long DEFAULT_CANDLE_LENGTH_IN_MINUTES = 30L;
    @Autowired
    CandleSticksService candleSticksService;
    @Autowired
    CandleStickControllerInputValidator validator;

    @GetMapping(path = "/candlesticks")
    public ResponseEntity<Collection<CandleStick>> getCandlesticks(@RequestParam("isin") @NotBlank String isin,
                                                                   @RequestParam("length") @Nullable Long candlestickLengthInMinutes,
                                                                   @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Nullable LocalDateTime timeFrom,
                                                                   @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Nullable LocalDateTime timeTo) {

        //if custom values are not provided. the endpoint will use default values to generate candlesticks:
        if (candlestickLengthInMinutes == null) {
            candlestickLengthInMinutes = DEFAULT_CANDLE_LENGTH_IN_MINUTES;
        }
        if (timeTo == null) {
            timeTo = LocalDateTime.now();
        }
        if (timeFrom == null) {
            timeFrom = timeTo.minusMinutes(candlestickLengthInMinutes);
        }
        validator.validateTheInput(isin, candlestickLengthInMinutes, timeFrom, timeTo);
        return ResponseEntity.ok(candleSticksService.getMinutesCandleStickForInterval(isin, candlestickLengthInMinutes, timeFrom, timeTo));

    }

}
