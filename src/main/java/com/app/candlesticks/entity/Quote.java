package com.app.candlesticks.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote implements Serializable {
    String isin;
    Double price;

    @CreatedDate
    private LocalDateTime timestamp;

}
