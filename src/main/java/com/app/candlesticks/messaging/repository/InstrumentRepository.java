package com.app.candlesticks.messaging.repository;

import com.app.candlesticks.entity.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface InstrumentRepository extends MongoRepository<Instrument, String> {

    void deleteByIsin(String isin);

    Instrument getByIsin(String isin);

}
