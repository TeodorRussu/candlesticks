package com.app.candlesticks.messaging.event;

import com.app.candlesticks.entity.Quote;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuoteEvent  implements Serializable {
    Quote data;
    QuoteEventType type;
}
