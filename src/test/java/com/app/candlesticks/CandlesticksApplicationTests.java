package com.app.candlesticks;

import com.app.candlesticks.configuration.SocketClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CandlesticksApplication.class, SocketClientConfig.class})
@ActiveProfiles("test")
class CandlesticksApplicationTests {

    @Test
    void contextLoads() {
    }

}
