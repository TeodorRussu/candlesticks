package com.app.candlesticks;

import com.app.candlesticks.entity.Quote;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@UtilityClass
public class TestingData {
    public static final String QUOTE_EVENT_MESSAGE_TEMPLATE = "{\n" +
            "  \"data\": {\n" +
            "    \"price\": 979.2824,\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"QUOTE\"\n" +
            "}";

    public static final String INSTRUMENT_ADD_EVENT_MESSAGE_TEMPLATE = "{\n" +
            "  \"data\": {\n" +
            "    \"description\": \"%s\",\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"ADD\"\n" +
            "}";

    public static final String INSTRUMENT_DELETE_EVENT_MESSAGE_TEMPLATE = "{\n" +
            "  \"data\": {\n" +
            "    \"description\": \"libris his metus voluptatum cras\",\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"DELETE\"\n" +
            "}";

}
