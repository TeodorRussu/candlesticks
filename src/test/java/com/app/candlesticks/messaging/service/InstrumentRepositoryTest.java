package com.app.candlesticks.messaging.service;

import com.app.candlesticks.entity.Instrument;
import com.app.candlesticks.messaging.repository.InstrumentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@DataMongoTest
class InstrumentRepositoryTest {

    @Autowired
    InstrumentRepository repository;

    @MockBean
    WebSocketSession instrumentStream;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Save one Instrument to the empty MongoDB. The DB must contain one item afterwards")
    void saveOneInstrumentToEmptyDb_ok() {
        Instrument instrument = new Instrument("AAA111111", "description");
        //given empty db
        List<Instrument> instruments = repository.findAll();
        Assertions.assertThat(instruments).isEmpty();

        //save one item, expected: the db will contain one item
        repository.save(instrument);
        instruments = repository.findAll();
        Assertions.assertThat(instruments.size()).isEqualTo(1);
    }

}

