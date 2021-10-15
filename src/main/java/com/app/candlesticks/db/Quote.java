package com.app.candlesticks.db;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Quote implements Serializable {
    Double price;
    String isin;
}
