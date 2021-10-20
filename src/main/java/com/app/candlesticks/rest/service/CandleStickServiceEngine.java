package com.app.candlesticks.rest.service;

import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.rest.dto.CandleStick;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class CandleStickServiceEngine {
    @NotNull
    public Deque<LocalDateTime> generateCandleSticksTimeIntervals(long candleLengthInMinutes, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Deque<LocalDateTime> candleSticksTimeFrames = new LinkedList<>();
        while (timeTo.isEqual(timeFrom) || timeTo.isAfter(timeFrom)) {
            candleSticksTimeFrames.push(timeTo);
            timeTo = timeTo.minusMinutes(candleLengthInMinutes);
        }
        return candleSticksTimeFrames;
    }

    public Collection<CandleStick> generateCandleSticksListFromQuotesBasedOnTimeFrames(List<Quote> timestampOrderedQuotes, Deque<LocalDateTime> candleSticksTimeFrames) {
        Deque<Quote> quotes = new LinkedList<>(timestampOrderedQuotes);
        Deque<CandleStick> candleSticks = new LinkedList<>();

        LocalDateTime timeFrom = candleSticksTimeFrames.pop();
        while (!candleSticksTimeFrames.isEmpty()) {
            LocalDateTime timeTo = candleSticksTimeFrames.pop();

            CandleStick candleStick = null;
            List<Quote> quotesForCandlestick = getQuotesForNextCandlestick(timeTo, quotes);

            //explanatory variables
            final boolean thereAreNoPrecedentCandlesticks_And_ThereAreNoQuotesForNextIntervalCandlestick
                    = quotesForCandlestick.isEmpty() && candleSticks.isEmpty();
            final boolean thereArePrecedentCandlesticks_And_ThereAreNoQuotesForTheTimeInterval
                    = quotesForCandlestick.isEmpty() && !candleSticks.isEmpty();
            boolean thereAreQuotesAvailableToBuildACandlestick = !quotesForCandlestick.isEmpty();

            //if there are no quotes between the interval(timeFrom ... timeTo), and no precedent candlesticks, continue to next interval.
            if (thereAreNoPrecedentCandlesticks_And_ThereAreNoQuotesForNextIntervalCandlestick) {
                timeFrom = timeTo;
                continue;
            }
            // if there are no quotes between the interval, next Candlestick is built based on precedent time interval Candlestick
            if (thereArePrecedentCandlesticks_And_ThereAreNoQuotesForTheTimeInterval) {
                candleStick = createCandleStickBasedOnPrecedentCandlestickClosingPrice(candleSticks);
                setCandlestickTimestamp(timeFrom, timeTo, candleStick);
            }
            //if there are quotes between the interval(timeFrom ... timeTo), a candlestick is built.
            if (thereAreQuotesAvailableToBuildACandlestick) {
                CandleStick precedent = candleSticks.peekLast();
                candleStick = convertQuotesToCandleStick(quotesForCandlestick, precedent);
                setCandlestickTimestamp(timeFrom, timeTo, candleStick);
            }
            candleSticks.add(candleStick);
            timeFrom = timeTo;
        }
        return candleSticks;
    }

    private void setCandlestickTimestamp(LocalDateTime timeFrom, LocalDateTime timeTo, CandleStick candleStick) {
        candleStick.setOpenTimestamp(timeFrom);
        candleStick.setCloseTimestamp(timeTo);
    }

    @NotNull
    private CandleStick createCandleStickBasedOnPrecedentCandlestickClosingPrice(Deque<CandleStick> candleSticks) {
        CandleStick candleStick = new CandleStick();
        Double price = candleSticks.getLast().getClosingPrice();

        candleStick.setLowPrice(price);
        candleStick.setHighPrice(price);
        candleStick.setOpenPrice(price);
        candleStick.setClosingPrice(price);

        return candleStick;
    }

    private List<Quote> getQuotesForNextCandlestick(LocalDateTime timeTo, Deque<Quote> result) {
        List<Quote> quotesSublist = new LinkedList<>();
        while (result.peekFirst() != null) {
            Quote quote = result.pollFirst();
            if (quote.getTimestamp().isAfter(timeTo)) {
                result.push(quote);
                break;
            } else {
                quotesSublist.add(quote);
            }
        }

        return quotesSublist;
    }

    private CandleStick convertQuotesToCandleStick(List<Quote> quotes, CandleStick precedent) {
        CandleStick candleStick = new CandleStick();

        Double openPrice = getCandleStickOpenPrice(quotes, precedent);
        candleStick.setOpenPrice(openPrice);

        candleStick.setClosingPrice(getPriceOfTheLastQuote(quotes));

        setCandleStickHighAndLowPrices(quotes, candleStick);

        return candleStick;
    }

    private Double getCandleStickOpenPrice(List<Quote> quotes, CandleStick precedent) {
        Double openPrice;
        if (precedent != null)
            openPrice = precedent.getClosingPrice();
        else {
            openPrice = quotes.get(0).getPrice();
        }

        return openPrice;
    }

    private void setCandleStickHighAndLowPrices(List<Quote> quotes, CandleStick candleStick) {
        Comparator<Quote> priceComparator = Comparator.comparing(Quote::getPrice);
        quotes.sort(priceComparator);

        Quote first = quotes.get(0);
        Quote last = quotes.get(quotes.size() - 1);

        candleStick.setHighPrice(last.getPrice());
        candleStick.setLowPrice(first.getPrice());
    }

    private Double getPriceOfTheLastQuote(List<Quote> quotes) {
        Quote lastQuote = quotes.get(quotes.size() - 1);

        return lastQuote.getPrice();
    }
}
