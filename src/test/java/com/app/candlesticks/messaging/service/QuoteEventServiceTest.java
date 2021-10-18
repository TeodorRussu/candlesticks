package com.app.candlesticks.messaging.service;

import com.app.candlesticks.entity.Instrument;
import com.app.candlesticks.entity.Quote;
import com.app.candlesticks.messaging.repository.InstrumentRepository;
import com.app.candlesticks.messaging.repository.QuoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static com.app.candlesticks.TestingData.QUOTE_EVENT_MESSAGE_TEMPLATE;

@DataMongoTest
class QuoteEventServiceTest {

    @MockBean
    WebSocketSession instrumentStream;

    QuoteEventService quoteEventService;

    ObjectMapper mapper;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    InstrumentRepository instrumentRepository;

    @BeforeEach
    void setUp() {
        quoteEventService = new QuoteEventService();
        quoteEventService.setInstrumentRepository(instrumentRepository);
        quoteEventService.setQuoteRepository(quoteRepository);
        mapper = new ObjectMapper();
        quoteEventService.setMapper(mapper);
    }

    @AfterEach
    void tearDown() {
        instrumentRepository.deleteAll();
        quoteRepository.deleteAll();
    }

    @Test
    @DisplayName("" +
            "Save an Instrument with a given ISIN (isin 1) into an empty MongoDb. " +
            "Save 2 Quotes with same ISIN. " +
            "Try to save a quote for a ISIN (isin 2) which is not present in the isin collection" +
            "The Quotes is not added")
    void testAddQuoteForNonExistingIsin_QuoteIsNotSaved() throws
            JsonProcessingException {
        final String isinOne = "AAA111111";
        final String isinTwo = "AAA222222";

        //given empty db
        List<Instrument> instruments = instrumentRepository.findAll();
        Assertions.assertThat(instruments).isEmpty();

        List<Quote> quotes = quoteRepository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        //save one instrument, expected: the db will contain one item
        Instrument instrument = new Instrument(isinOne, "description");
        instrumentRepository.save(instrument);
        instruments = instrumentRepository.findAll();
        Assertions.assertThat(instruments.size()).isEqualTo(1);

        //add 2 quotes for existing ISIN, expected: the db will contain both items.
        quoteEventService.handleEvent(String.format(QUOTE_EVENT_MESSAGE_TEMPLATE, isinOne));
        quoteEventService.handleEvent(String.format(QUOTE_EVENT_MESSAGE_TEMPLATE, isinOne));
        quotes = quoteRepository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(2);

        //Try to add a Quote for a ISIN which don't exist in the database.
        //Expected: the item will be not saved
        quoteEventService.handleEvent(String.format(QUOTE_EVENT_MESSAGE_TEMPLATE, isinTwo));
        quotes = quoteRepository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(2);
        Assertions.assertThat(quotes.get(0).getIsin()).isEqualTo(isinOne);
        Assertions.assertThat(quotes.get(1).getIsin()).isEqualTo(isinOne);
    }

}