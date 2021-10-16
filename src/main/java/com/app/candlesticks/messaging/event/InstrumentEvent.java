package com.app.candlesticks.messaging.event;

import com.app.candlesticks.entity.Instrument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentEvent implements Serializable {
    InstrumentEventType type;
    Instrument data;
}

