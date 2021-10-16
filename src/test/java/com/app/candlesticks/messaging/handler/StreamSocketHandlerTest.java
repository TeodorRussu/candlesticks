package com.app.candlesticks.messaging.handler;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.app.candlesticks.CandlesticksApplication;
import com.app.candlesticks.config.SocketClientConfig;
import com.app.candlesticks.repo.InstrumentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static com.app.candlesticks.TestingData.INSTRUMENT_ADD_EVENT_MESSAGE_TEMPLATE;
import static com.app.candlesticks.messaging.handler.StreamSocketHandler.MESSAGE_HANDLING_EXCEPTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(classes = {CandlesticksApplication.class, SocketClientConfig.class, InstrumentRepositoryMockConfig.class})
@ActiveProfiles("test")
class StreamSocketHandlerTest {

    @Autowired
    InstrumentStreamSocketHandler instrumentStreamSocketHandler;

    @Autowired
    WebSocketSession instrumentStream;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Test
    @DisplayName("If some exception occurs during handling, " +
            "the exception must be logged for further application improvements" +
            "Scenario: The repository is mocked to throw a RuntimeException when save method is invoked." +
            "The exception is logged, and we capture and assert that the log message is the expected one")
    void testHandlerExceptionLogging() throws Exception {
        // get Logback Logger
        Logger fooLogger = (Logger) LoggerFactory.getLogger(StreamSocketHandler.class);

        // create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // add the appender to the logger
        fooLogger.addAppender(listAppender);

        // prepare the message and call the handler method
        TextMessage message = new TextMessage(String.format(INSTRUMENT_ADD_EVENT_MESSAGE_TEMPLATE, "description", "AAA111"));
        instrumentStreamSocketHandler.handleTextMessage(instrumentStream, message);

        //capture the log, compare the error message to the expected template
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(MESSAGE_HANDLING_EXCEPTION_MESSAGE, logsList.get(1).getMessage());
    }
}

@Profile("test")
@Configuration
class InstrumentRepositoryMockConfig {
    @Bean
    @Primary
    public InstrumentRepository nameService() {
        final InstrumentRepository instrumentRepositoryMock = Mockito.mock(InstrumentRepository.class);
        doThrow(new RuntimeException()).when(instrumentRepositoryMock).save(any());
        return instrumentRepositoryMock;
    }
}