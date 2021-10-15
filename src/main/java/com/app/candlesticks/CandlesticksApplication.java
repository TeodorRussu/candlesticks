package com.app.candlesticks;

import com.app.candlesticks.db.Quote;
import com.app.candlesticks.repo.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CandlesticksApplication implements CommandLineRunner {

    @Autowired
    private QuoteRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(CandlesticksApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        for (Quote quote : repository.findAllByIsin("KO47226M1M46")) {
            System.out.println(quote);
        }
        System.out.println();
    }

}
