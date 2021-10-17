package com.app.candlesticks.controller;

import com.app.candlesticks.dto.CandleStick;
import org.apache.commons.validator.routines.ISINValidator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CandlestickManager {



    @GetMapping(path = "/candlesticks/{isin}")
    public List<CandleStick> getCandlesticks(@PathVariable("isin") @NonNull String isin) {

        org.apache.commons.validator.routines.ISINValidator validator = ISINValidator.getInstance(false);
        boolean isIsinValid = validator.isValid(isin);

//      validation error (Status.BAD_REQUEST).body("{'reason': 'missing_isin'}")
        return null;
    }
}
