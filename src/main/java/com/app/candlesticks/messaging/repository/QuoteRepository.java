package com.app.candlesticks.messaging.repository;

import com.app.candlesticks.entity.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface QuoteRepository extends MongoRepository<Quote, String> {

    List<Quote> findAllByIsin(String isin);

    void deleteAllByIsin(String isin);

    List<Quote> findAllByIsinAndTimestampBetweenOrderByTimestamp(String isin, LocalDateTime startDate, LocalDateTime endDate);

}