package com.app.candlesticks.messaging.event;

import com.app.candlesticks.messaging.data.Instrument;
import lombok.Data;

import java.io.Serializable;

//
//{
//        "data": {
//        "description": "dicam dicta maiorum",
//        "isin": "JMK610480372"
//        },
//        "type": "ADD"
//        }


@Data
public class InstrumentEvent implements Serializable {
    InstrumentEventType type;
    Instrument data;

}

enum InstrumentEventType {
    ADD,
    DELETE
}
