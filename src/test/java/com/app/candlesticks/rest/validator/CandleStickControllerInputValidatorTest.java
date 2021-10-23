package com.app.candlesticks.rest.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


class CandleStickControllerInputValidatorTest {

    CandleStickControllerInputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CandleStickControllerInputValidator();
    }

    @Test
    void testEmptyISIN() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validator.validateTheInput("", 30L, LocalDateTime.parse("2021-10-12T12:25:49"), LocalDateTime.parse("2021-10-17T12:25:49"))
        );
    }

    @Test
    void testTimeFrom_isAfter_timeTo() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validator.validateTheInput("AA11", 30L, LocalDateTime.parse("2021-10-12T12:25:49"), LocalDateTime.parse("2021-10-10T12:25:49"))
        );
    }

    @Test
    void testCandleStickLength_isBiggerThanTimeRange() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validator.validateTheInput("AA11", 600L, LocalDateTime.parse("2021-10-12T11:25:49"), LocalDateTime.parse("2021-10-12T12:25:49"))
        );
    }

    @Test
    void testNegativeCandleStickLength() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validator.validateTheInput("AA11", -10L, LocalDateTime.parse("2021-10-12T11:25:49"), LocalDateTime.parse("2021-10-12T12:25:49"))
        );
    }

}
