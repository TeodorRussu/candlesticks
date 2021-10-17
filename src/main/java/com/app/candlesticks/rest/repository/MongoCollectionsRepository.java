package com.app.candlesticks.rest.repository;

import com.app.candlesticks.configuration.MongoConfig;
import com.app.candlesticks.entity.Quote;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

@Component
public class MongoCollectionsRepository {

    public static final String ISIN = "isin";
    public static final String TIMESTAMP = "timestamp";
    public static final String MONGO_DB_QUOTES_COLLECTION_NAME = "quote";
    public static final String MONGO_DATABASE_NAME = "candlesticks";
    public static final String PRICE = "price";

    @Autowired
    MongoConfig mongoConfig;
    MongoCollection<Document> collection;

    //A query based on MongoDb filters is needed to retrieve only Quote documents within the specified time range
    public Deque<Quote> getDocumentsFromDatabase(String isin, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Bson filter = and(eq(ISIN, isin), and(gte(TIMESTAMP, timeFrom), lte(TIMESTAMP, timeTo)));
        ArrayList<Document> quotesAsListOfDocuments = collection.find(filter).into(new ArrayList<>());

        return convertDocumentListToSortedDequeOfQuotes(isin, quotesAsListOfDocuments);
    }

    private Deque<Quote> convertDocumentListToSortedDequeOfQuotes(String isin, ArrayList<Document> quotesAsListOfDocuments) {
        Comparator<Quote> timeStampComparator = Comparator.comparing(Quote::getTimestamp);

        //convert the raw MongoDb documents list to a ordered (based on timestamp) deque of Quotes
        return quotesAsListOfDocuments
                .stream()
                .map(document -> {
                    Double price = document.getDouble(PRICE);
                    LocalDateTime timestamp = LocalDateTime.ofInstant(
                            document.getDate(TIMESTAMP).toInstant(), ZoneId.systemDefault());
                    return new Quote(isin, price, timestamp);
                })
                .sorted(timeStampComparator)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @PostConstruct
    private void getDocumentMongoCollection() {
        collection = mongoConfig.mongoClient()
                .getDatabase(MONGO_DATABASE_NAME)
                .getCollection(MONGO_DB_QUOTES_COLLECTION_NAME);
    }
}
