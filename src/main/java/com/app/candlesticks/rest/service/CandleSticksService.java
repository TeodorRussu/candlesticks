package com.app.candlesticks.rest.service;

import com.app.candlesticks.rest.dto.CandleStick;
import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.rest.repository.MongoCollectionsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class CandleSticksService {

    @Autowired
    MongoCollectionsRepository mongoCollectionsRepository;

    public Collection<CandleStick> getMinutesCandleStickForInterval(String isin, long candleLengthInMinutes, String timeFromValue, String timeToValue) {

        LocalDateTime timeFrom = LocalDateTime.parse(timeFromValue);
        LocalDateTime timeTo = LocalDateTime.parse(timeToValue);

        Deque<Quote> allQuotesOrderedByTimestamp = mongoCollectionsRepository.getDocumentsFromDatabase(isin, timeFrom, timeTo);
        Deque<LocalDateTime> candleSticksTimeFrames = generateCandleSticksTimeIntervals(candleLengthInMinutes, timeFrom, timeTo);

        return generateCandleSticksListFromQuotesBasedOnTimeFrames(allQuotesOrderedByTimestamp, candleSticksTimeFrames);
    }


    @NotNull
    private Deque<LocalDateTime> generateCandleSticksTimeIntervals(long candleLengthInMinutes, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Deque<LocalDateTime> candleSticksTimeFrames = new LinkedList<>();
        while (timeTo.isAfter(timeFrom)) {
            candleSticksTimeFrames.push(timeTo);
            timeTo = timeTo.minusMinutes(candleLengthInMinutes);
        }
        return candleSticksTimeFrames;
    }

    private Collection<CandleStick> generateCandleSticksListFromQuotesBasedOnTimeFrames(Deque<Quote> allQuotesTimestampOrdered, Deque<LocalDateTime> candleSticksTimeFrames) {
        Deque<CandleStick> candleSticks = new LinkedList<>();

        LocalDateTime timeFrom = candleSticksTimeFrames.pop();
        while (!candleSticksTimeFrames.isEmpty()) {
            LocalDateTime timeTo = candleSticksTimeFrames.pop();

            CandleStick candleStick = null;
            List<Quote> quotesForCandlestick = getQuotesForNextCandlestick(timeTo, allQuotesTimestampOrdered);

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
                candleStick = convertDocumentsToCandleStick(quotesForCandlestick);
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
        if (!quotesSublist.isEmpty() && !result.isEmpty()) {
            result.push(quotesSublist.get(quotesSublist.size() - 1));
        }

        return quotesSublist;
    }


    //methods used to convert a list of Quotes into a Candlestick
    private CandleStick convertDocumentsToCandleStick(List<Quote> quotes) {
        CandleStick candleStick = new CandleStick();
        setCandleStickOpeningAndClosingPrice(quotes, candleStick);
        setCandleStickHighestAndLowestPrices(quotes, candleStick);

        return candleStick;
    }

    private void setCandleStickHighestAndLowestPrices(List<Quote> quotes, CandleStick candleStick) {
        Comparator<Quote> priceComparator = Comparator.comparing(Quote::getPrice);
        quotes.sort(priceComparator);

        Quote first = quotes.get(0);
        Quote last = quotes.get(quotes.size() - 1);

        candleStick.setHighPrice(last.getPrice());
        candleStick.setLowPrice(first.getPrice());
    }

    private void setCandleStickOpeningAndClosingPrice(List<Quote> quotes, CandleStick candleStick) {
        Quote first = quotes.get(0);
        Quote last = quotes.get(quotes.size() - 1);

        candleStick.setOpenPrice(first.getPrice());
        candleStick.setClosingPrice(last.getPrice());
    }

}
