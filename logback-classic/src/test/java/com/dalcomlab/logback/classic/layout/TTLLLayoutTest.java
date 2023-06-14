package com.dalcomlab.logback.classic.layout;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TTLLLayoutTest {

    LoggerContext context = new LoggerContext();
    Logger logger = context.getLogger(TTLLLayoutTest.class);
    TTLLLayout layout = new TTLLLayout();

    @Before
    public void setUp() {
        layout.setContext(context);
        layout.start();
    }

    @Test
    public void nullMessage() {
        LoggingEvent event = new LoggingEvent("", logger, Level.INFO, null, null, null);
        event.setTimeStamp(0);
        String result = layout.doLayout(event);
        
        assertEquals("[main] INFO com.dalcomlab.logback.classic.layout.TTLLLayoutTest - null", result.substring(13).trim());
    }
}
