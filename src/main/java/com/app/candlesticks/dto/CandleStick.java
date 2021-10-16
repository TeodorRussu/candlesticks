package com.app.candlesticks.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandleStick {
    LocalDateTime openTimestamp;
    LocalDateTime closeTimestamp;
    Double openPrice;
    Double highPrice;
    Double lowPrice;
    Double closingPrice;
}
