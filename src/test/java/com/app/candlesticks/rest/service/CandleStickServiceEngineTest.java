package com.app.candlesticks.rest.service;

import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.rest.dto.CandleStick;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class CandleStickServiceEngineTest {

    CandleStickServiceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CandleStickServiceEngine();
    }

    @Test
    @DisplayName("Scenario: dateFrom + 2 * candlestick length = dateTo" +
            "Output must contain 3 entries:" +
            "first -> equals to dateFrom" +
            "last -> equals to DateTo" +
            "middle entry is after DateFrom, and beforeDateTo")
    void testGenerateCandleSticksTimeIntervals_intervalIsEqualToCandleLengthTimes2() {
        int candleLength = 60;

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T22:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleLength * 2);

        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleLength, dateFrom, dateTo);
        Assertions.assertThat(intervals.size()).isEqualTo(3);
        Assertions.assertThat(intervals.getFirst()).isEqualTo(dateFrom);
        Assertions.assertThat(intervals.getLast()).isEqualTo(dateTo);
    }

    @Test
    @DisplayName("Scenario: dateFrom + candlestick length < dateTo" +
            "Output must contain 2 entries: first entry time value is after dateFrom. The second equals to dateTo")
    void testGenerateCandleSticksTimeIntervals_intervalIsLessThanCandleLengthTimes2() {
        int candleLength = 60;

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes((long) (candleLength * 1.5));

        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleLength, dateFrom, dateTo);
        Assertions.assertThat(intervals.size()).isEqualTo(2);
        Assertions.assertThat(intervals.getFirst()).isAfter(dateFrom);
        Assertions.assertThat(intervals.getLast()).isEqualTo(dateTo);
    }

    @Test
    @DisplayName("Scenario: dateFrom + candlestick length = dateTo" +
            "Output must contain 2 entries equals to dateFrom, respectively dateTo")
    void testGenerateCandleSticksTimeIntervals_intervalIsEqualsToCandleLengthTimes2() {
        int candleLength = 60;

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleLength);

        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleLength, dateFrom, dateTo);
        Assertions.assertThat(intervals.size()).isEqualTo(2);
        Assertions.assertThat(intervals.getFirst()).isEqualTo(dateFrom);
        Assertions.assertThat(intervals.getLast()).isEqualTo(dateTo);
    }


    @Test
    @DisplayName("Scenario: create 10 time intervals. " +
            "Create 6 quotes that fit the first interval, and 6 quotes that fit last interval." +
            "Expected: First and last candlestick will be generated based on quotes values." +
            "Other candlesticks will be generated based on the first candlestick")
    void testGenerateCandleSticksListFromQuotesBasedOnTimeFrames_I___I() {
        int candleStickLength = 60;
        String isin = "A100";

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleStickLength * 10);
        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleStickLength, dateFrom, dateTo);

        //add quotes for first interval only
        List<Quote> quotes = new ArrayList<>();
        LocalDateTime quoteDate = dateFrom.plusMinutes(5);
        double price = 1.0;
        while (quoteDate.isBefore(dateFrom.plusMinutes(candleStickLength))) {
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
            price++;
        }
        //add quotes for the last interval only
        quoteDate = dateTo.minusMinutes(candleStickLength - 5);
        while (quoteDate.isBefore(dateTo)) {
            price++;
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
        }
        Assertions.assertThat(quotes.size()).isEqualTo(12);

        Deque<CandleStick> outputCandlesticks = new LinkedList<>(engine.generateCandleSticksListFromQuotesBasedOnTimeFrames(quotes, intervals));
        Assertions.assertThat(outputCandlesticks.size()).isEqualTo(10);

        //Expect that output will be a list where each element closing timestamp and price is equal to next element opening timestamp and price
        CandleStick headElement = outputCandlesticks.pollFirst();
        while (!outputCandlesticks.isEmpty()) {
            CandleStick nextElement = outputCandlesticks.pollFirst();
            Assertions.assertThat(headElement.getClosingPrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseTimestamp()).isEqualTo(nextElement.getOpenTimestamp());
            headElement = nextElement;
        }

    }

    @Test
    @DisplayName("Scenario: create 10 time intervals. " +
            "Create 6 quotes that fit the first interval, and 6 quotes that fit last interval." +
            "Create 6 quotes that fit interval 5." +
            "" +
            "Expected: output: 10 candlesticks." +
            "Candlestick 0, 4 and 9 will be generated based on quotes values." +
            "Candlesticks 1-3 will be generated based on first Candlestick" +
            "Candlestick 5-8 will be generated based on Candlestick 5")
    void testGenerateCandleSticksListFromQuotesBasedOnTimeFrames_I___I____I() {
        int candleStickLength = 60;
        String isin = "A100";

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleStickLength * 10);
        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleStickLength, dateFrom, dateTo);

        //add quotes for first interval only
        List<Quote> quotes = new ArrayList<>();
        LocalDateTime quoteDate = dateFrom.plusMinutes(5);
        double price = 1.0;
        while (quoteDate.isBefore(dateFrom.plusMinutes(candleStickLength))) {
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
            price++;
        }
        //add quotes for the fifth interval
        quoteDate = dateFrom.plusMinutes(candleStickLength * 4 + 5);
        LocalDateTime dateToFifthInterval = dateFrom.plusMinutes(candleStickLength * 5);
        while (quoteDate.isBefore(dateToFifthInterval)) {
            price++;
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
        }
        //add quotes for the last interval only
        quoteDate = dateTo.minusMinutes(candleStickLength - 5);
        while (quoteDate.isBefore(dateTo)) {
            price++;
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
        }
        Assertions.assertThat(quotes.size()).isEqualTo(18);

        List<CandleStick> outputCandlesticks = new LinkedList<>(engine.generateCandleSticksListFromQuotesBasedOnTimeFrames(quotes, intervals));
        Assertions.assertThat(outputCandlesticks.size()).isEqualTo(10);

        for (int i = 1; i <= 3; i++) {
            Assertions.assertThat(outputCandlesticks.get(0).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getHighPrice());
            Assertions.assertThat(outputCandlesticks.get(0).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getLowPrice());
        }
        for (int i = 5; i <= 8; i++) {
            Assertions.assertThat(outputCandlesticks.get(4).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getHighPrice());
            Assertions.assertThat(outputCandlesticks.get(4).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getLowPrice());
        }

        Deque<CandleStick> outputCandlesticksQueue = new LinkedList<>(outputCandlesticks);
        //Expect that output will be a list where each element closing timestamp and price is equal to next element opening timestamp and price
        CandleStick headElement = outputCandlesticksQueue.pollFirst();
        while (!outputCandlesticksQueue.isEmpty()) {
            CandleStick nextElement = outputCandlesticksQueue.pollFirst();
            Assertions.assertThat(headElement.getClosingPrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseTimestamp()).isEqualTo(nextElement.getOpenTimestamp());
            headElement = nextElement;
        }

    }

    @Test
    @DisplayName("Scenario: create 10 time intervals. " +
            "Create 6 quotes that fit last interval." +
            "Create 6 quotes that fit interval 4." +
            "" +
            "Expected: output: 6 candlesticks." +
            "Candlestick 0 and 5 will be generated based on quotes values." +
            "Candlesticks 1-4 will be generated based on candlestick 0")
    void testGenerateCandleSticksListFromQuotesBasedOnTimeFrames____I____I() {
        int candleStickLength = 60;
        String isin = "A100";

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleStickLength * 10);
        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleStickLength, dateFrom, dateTo);

        //add quotes for the fifth interval
        List<Quote> quotes = new ArrayList<>();
        LocalDateTime quoteDate = dateFrom.plusMinutes(5);
        double price = 1.0;
        quoteDate = dateFrom.plusMinutes(candleStickLength * 4 + 5);
        LocalDateTime dateToFifthInterval = dateFrom.plusMinutes(candleStickLength * 5);
        while (quoteDate.isBefore(dateToFifthInterval)) {
            price++;
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
        }

        //add quotes for the last interval only
        quoteDate = dateTo.minusMinutes(candleStickLength - 5);
        while (quoteDate.isBefore(dateTo)) {
            price++;
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
        }
        Assertions.assertThat(quotes.size()).isEqualTo(12);

        List<CandleStick> outputCandlesticks = new LinkedList<>(engine.generateCandleSticksListFromQuotesBasedOnTimeFrames(quotes, intervals));
        Assertions.assertThat(outputCandlesticks.size()).isEqualTo(6);

        for (int i = 1; i <= 4; i++) {
            Assertions.assertThat(outputCandlesticks.get(0).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getHighPrice());
            Assertions.assertThat(outputCandlesticks.get(0).getClosingPrice()).isEqualTo(outputCandlesticks.get(i).getLowPrice());
        }

        Deque<CandleStick> outputCandlesticksQueue = new LinkedList<>(outputCandlesticks);
        //Expect that output will be a list where each element closing timestamp and price is equal to next element opening timestamp and price
        CandleStick headElement = outputCandlesticksQueue.pollFirst();
        while (!outputCandlesticksQueue.isEmpty()) {
            CandleStick nextElement = outputCandlesticksQueue.pollFirst();
            Assertions.assertThat(headElement.getClosingPrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseTimestamp()).isEqualTo(nextElement.getOpenTimestamp());
            headElement = nextElement;
        }

    }

    @Test
    @DisplayName("Scenario: create 10 time intervals. " +
            "Create quotes for all interval." +
            "Expected: output: 10 different candlesticks.")
    void testGenerateCandleSticksListFromQuotesBasedOnTimeFrames_IIIIIIIIII() {
        int candleStickLength = 60;
        String isin = "A100";

        LocalDateTime dateTo = LocalDateTime.parse("2021-10-20T10:00:00.000000");
        LocalDateTime dateFrom = dateTo.minusMinutes(candleStickLength * 10);
        Deque<LocalDateTime> intervals = engine.generateCandleSticksTimeIntervals(candleStickLength, dateFrom, dateTo);

        //add quotes for first interval only
        List<Quote> quotes = new ArrayList<>();
        LocalDateTime quoteDate = dateFrom.plusMinutes(5);
        double price = 1.0;
        while (quoteDate.isBefore(dateTo)) {
            quotes.add(new Quote(isin, price, quoteDate));
            quoteDate = quoteDate.plusMinutes(10);
            price++;
        }

        Assertions.assertThat(quotes.size()).isEqualTo(60);

        List<CandleStick> outputCandlesticks = new LinkedList<>(engine.generateCandleSticksListFromQuotesBasedOnTimeFrames(quotes, intervals));
        Assertions.assertThat(outputCandlesticks.size()).isEqualTo(10);

        Deque<CandleStick> outputCandlesticksQueue = new LinkedList<>(outputCandlesticks);
        //Expect that output will be a list where each element closing timestamp and price is equal to next element opening timestamp and price
        CandleStick headElement = outputCandlesticksQueue.pollFirst();
        while (!outputCandlesticksQueue.isEmpty()) {
            CandleStick nextElement = outputCandlesticksQueue.pollFirst();
            Assertions.assertThat(headElement.getClosingPrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseTimestamp()).isEqualTo(nextElement.getOpenTimestamp());
            Assertions.assertThat(headElement.getHighPrice()).isNotEqualTo(nextElement.getHighPrice());
            Assertions.assertThat(headElement.getLowPrice()).isNotEqualTo(nextElement.getLowPrice());
            headElement = nextElement;
        }

    }

}