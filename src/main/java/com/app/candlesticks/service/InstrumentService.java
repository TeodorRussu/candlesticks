package com.app.candlesticks.service;

import com.app.candlesticks.dto.CandleStick;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstrumentService {

    @Autowired
    ObjectMapper mapper;


    public List<CandleStick> getCandlesticks(String identifier) {
        return null;
    }


    public CandleStick save(CandleStick data) {
        return null;
    }
}
