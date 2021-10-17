package com.app.candlesticks.rest.controller;

import com.app.candlesticks.rest.dto.CandleStick;
import com.app.candlesticks.rest.service.CandleSticksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class CandleSticksController {

    @Autowired
    CandleSticksService candleSticksService;

    @GetMapping(path = "/candlesticks")
    public ResponseEntity<Collection<CandleStick>> getCandlesticks(@RequestParam("isin") @NonNull String isin,
                                                                   @RequestParam("length") @Nullable Long candleLengthInMinutes,
                                                                   @RequestParam("from") @Nullable String timeFrom,
                                                                   @RequestParam("to") @Nullable String timeTo) {

        //if custom values are not provided. the endpoint will use default values:
        if (candleLengthInMinutes == null) {
            candleLengthInMinutes = 30L;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (timeTo == null) {
            timeTo = currentTime.toString();
        }
        if (timeFrom == null) {
            timeFrom = currentTime.minusMinutes(30).toString();
        }

        final Collection<CandleStick> minutesCandleStickForInterval = candleSticksService.getMinutesCandleStickForInterval(isin, candleLengthInMinutes, timeFrom, timeTo);
        return ResponseEntity.ok(minutesCandleStickForInterval);

    }
}
