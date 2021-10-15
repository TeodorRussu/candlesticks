package com.app.candlesticks.messaging.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class Instrument implements Serializable {
    String isin;
    String description;
}
