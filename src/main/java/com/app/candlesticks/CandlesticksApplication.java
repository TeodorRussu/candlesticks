package com.app.candlesticks;

import com.app.candlesticks.repo.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CandlesticksApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandlesticksApplication.class, args);
    }

}
