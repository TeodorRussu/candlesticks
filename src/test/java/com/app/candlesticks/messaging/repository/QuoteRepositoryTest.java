package com.app.candlesticks.messaging.repository;

import com.app.candlesticks.entity.Quote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@DataMongoTest
class QuoteRepositoryTest {

    @Autowired
    QuoteRepository repository;

    @MockBean
    WebSocketSession instrumentStream;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Save one Quote to the empty MongoDB. The DB must contain one item afterwards")
    void saveOneInstrumentToEmptyDb_ok() {
        Quote quote = new Quote("AAA111111", 1.2222, LocalDateTime.now());
        //given empty db
        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        //save one item, expected: the db will contain one item
        repository.save(quote);
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Save Quote (item 1) for one ISIN (ISIN 1) to the empty MongoDB." +
            "Save Quotes (items 2 and 3) for another ISIN (ISIN 2)." +
            "Remove all quotes (2 and 3) by ISIN" +
            "Result: The DB must contain one item (item 1) afterwards.")
    void deleteAllByIsin_ok() {
        String isinOne = "AAA111111";
        String isinTwo = "AAA222222";

        Quote quoteOne = new Quote(isinOne, 1.111, LocalDateTime.now());
        Quote quoteTwo = new Quote(isinTwo, 1.222, LocalDateTime.now());
        Quote quoteThree = new Quote(isinTwo, 1.333, LocalDateTime.now());

        //given empty db
        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        //save all items, expected: the db will contain one item
        repository.saveAll(List.of(quoteOne, quoteTwo, quoteThree));
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(3);

        //delete all by isin 2. Expected: database contains only one item (for ISIN 1)
        repository.deleteAllByIsin(isinTwo);
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(1);
        Assertions.assertThat(quotes.get(0).getIsin()).isEqualTo(isinOne);
    }

    @Test
    @DisplayName("Save Quote (item 1) for one ISIN (ISIN 1) to the empty MongoDB." +
            "Save Quotes (items 2 and 3) for another ISIN (ISIN 2)." +
            "Find all quotes by ISIN 2" +
            "Result: The output list will have 2 items.")
    void findAllByIsin_ok() {
        String isinOne = "AAA111111";
        String isinTwo = "AAA222222";

        Quote quoteOne = new Quote(isinOne, 1.111, LocalDateTime.now());
        Quote quoteTwo = new Quote(isinTwo, 1.222, LocalDateTime.now());
        Quote quoteThree = new Quote(isinTwo, 1.333, LocalDateTime.now());

        //given empty db
        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        //save all items, expected: the db will contain one item
        repository.saveAll(List.of(quoteOne, quoteTwo, quoteThree));
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(3);

        //find all by isin 2. Expected: the output list contains 2 items (for ISIN 2)
        quotes = repository.findAllByIsin(isinTwo);
        Assertions.assertThat(quotes.size()).isEqualTo(2);
        Assertions.assertThat(quotes.get(0).getIsin()).isEqualTo(isinTwo);
        Assertions.assertThat(quotes.get(1).getIsin()).isEqualTo(isinTwo);
    }

}