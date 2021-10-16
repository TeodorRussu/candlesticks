package com.app.candlesticks.repo;

import com.app.candlesticks.entity.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface InstrumentRepository extends MongoRepository<Instrument, String> {

    public void deleteByIsin(String isin);

    public Instrument getByIsin(String isin);

}
