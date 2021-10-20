package com.app.candlesticks.rest.controller;

import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.messaging.repository.QuoteRepository;
import com.app.candlesticks.rest.dto.CandleStick;
import com.app.candlesticks.rest.service.CandleSticksService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static com.app.candlesticks.rest.controller.CandleSticksController.DEFAULT_CANDLE_LENGTH_IN_MINUTES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CandleSticksControllerTest {

    public static final String ENDPOINT_URL_TEMPLATE = "/candlesticks?isin=%s&length=%d&from=%s&to=%s";
    public static final String ENDPOINT_URL_TEMPLATE_ISIN_ONLY = "/candlesticks?isin=%s";
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Mock
    QuoteRepository quoteRepository;

    @Autowired
    CandleSticksService service;

    String isin;

    List<Quote> quotes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service.setQuoteRepository(quoteRepository);
    }

    @Test
    @DisplayName("Happy flow test. Candlesticks are generated for all time intervals")
    void testHappyFlowWithParameters() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(10000);

        //setup the mock db response
        List<Quote> quotes = generateQuotesBetween(isin, timeFrom, timeTo);
        doReturn(quotes).when(quoteRepository).findAllByIsinAndTimestampBetweenOrderByTimestamp(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        //setup the url
        String url = String.format(ENDPOINT_URL_TEMPLATE, isin, 60, timeFrom, timeTo);
        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStick> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStick>>() {
        });

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
    @DisplayName("Candlesticks are generated for all time intervals. " +
            "For ranges without candles available, the candlesticks are generated based on previous ones")
    void testHappyFlowWithParameters_SomeCandlesWillBeGeneratedBasedOnPrecedentOnes() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(10000);

        //setup the mock db response
        List<Quote> allQuotes = generateQuotesBetween(isin, timeFrom, timeTo);
        List<Quote> quotes = new ArrayList<>();
        quotes.addAll(allQuotes.subList(0, 100));
        quotes.addAll(allQuotes.subList(9500, 10000));

        doReturn(quotes).when(quoteRepository).findAllByIsinAndTimestampBetweenOrderByTimestamp(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        //setup the url
        String url = String.format(ENDPOINT_URL_TEMPLATE, isin, 60, timeFrom, timeTo);
        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStick> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStick>>() {
        });

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
    @DisplayName("Only isin parameter is sent to the endpoint request. " +
            "Default values are used to generate the candlestick." +
            "Only one candlestick is generated")
    void testHappyFlowWithDefaultParameters_existingData_andOneCandleIsReturned() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(DEFAULT_CANDLE_LENGTH_IN_MINUTES);
        String url = String.format(ENDPOINT_URL_TEMPLATE_ISIN_ONLY, isin);

        //setup the mock db response
        List<Quote> quotes = generateQuotesBetween(isin, timeFrom, timeTo);
        doReturn(quotes).when(quoteRepository).findAllByIsinAndTimestampBetweenOrderByTimestamp(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStick> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStick>>() {
        });
        Assertions.assertThat(outputCandlesticks.size()).isOne();
    }

    @Test
    @DisplayName("Send request with blank isin. Bad request error is returned")
    void testRequestWithNoISIN_andGetNotFoundError() throws Exception {
        String url = String.format(ENDPOINT_URL_TEMPLATE_ISIN_ONLY, "");

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());

    }

    private List<Quote> generateQuotesBetween(String isin, LocalDateTime timeFrom, LocalDateTime timeTo) {
        List<Quote> quotes = new ArrayList<>();

        double price = 0.001;

        while (timeFrom.isBefore(timeTo)) {
            Quote quote = new Quote(isin, price, timeFrom);
            quotes.add(quote);
            price++;
            timeFrom = timeFrom.plusMinutes(1);

        }

        Comparator<Quote> timeStampComparator = Comparator.comparing(Quote::getTimestamp);
        quotes.sort(timeStampComparator);
        return quotes;
    }
}