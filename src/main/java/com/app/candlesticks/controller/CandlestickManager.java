package com.app.candlesticks.controller;

import com.app.candlesticks.dto.CandleStick;
import com.app.candlesticks.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CandlestickManager {

    @Autowired
    InstrumentService service;

    @GetMapping
    public List<CandleStick> getCandlesticks(String isin) {

//      validation error (Status.BAD_REQUEST).body("{'reason': 'missing_isin'}")
        return service.getCandlesticks(isin);
    }
}
