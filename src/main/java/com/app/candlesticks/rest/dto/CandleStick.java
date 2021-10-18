package com.app.candlesticks.rest.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CandleStick implements Serializable{
    LocalDateTime openTimestamp;
    LocalDateTime closeTimestamp;
    Double openPrice;
    Double highPrice;
    Double lowPrice;
    Double closingPrice;
}
