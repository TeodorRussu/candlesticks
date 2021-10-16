package com.app.candlesticks.repo;

import com.app.candlesticks.entity.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface QuoteRepository extends MongoRepository<Quote, String> {

    public List<Quote> findAllByIsin(String isin);

    public void deleteAllByIsin(String isin);
}