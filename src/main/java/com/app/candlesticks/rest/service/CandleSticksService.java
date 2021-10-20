package com.app.candlesticks.rest.service;

import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.messaging.repository.QuoteRepository;
import com.app.candlesticks.rest.dto.CandleStick;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Deque;
import java.util.List;


@Service
@Data
public class CandleSticksService {

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    CandleStickServiceEngine engine;

    public Collection<CandleStick> getMinutesCandleStickForInterval(String isin, long candleLengthInMinutes, String timeFromValue, String timeToValue) {

        LocalDateTime timeFrom = LocalDateTime.parse(timeFromValue);
        LocalDateTime timeTo = LocalDateTime.parse(timeToValue);

        List<Quote> quotesOrderedByTimestamp = quoteRepository.findAllByIsinAndTimestampBetweenOrderByTimestamp(isin, timeFrom, timeTo);

        Deque<LocalDateTime> candleSticksTimeFrames = engine.generateCandleSticksTimeIntervals(candleLengthInMinutes, timeFrom, timeTo);

        return engine.generateCandleSticksListFromQuotesBasedOnTimeFrames(quotesOrderedByTimestamp, candleSticksTimeFrames);
    }

}
