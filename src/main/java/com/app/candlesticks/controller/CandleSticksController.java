package com.app.candlesticks.controller;

import com.app.candlesticks.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CandleSticksController {

    @Autowired
    InstrumentService candleStickService;
}
