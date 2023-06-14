/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package com.dalcomlab.logback.classic;

import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.PatternLayout;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.core.read.ListAppender;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoggerMessageFormattingTest {

    LoggerContext lc;
    ListAppender<ILoggingEvent> listAppender;

    @Before
    public void setUp() {
        lc = new LoggerContext();
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        listAppender = new ListAppender<ILoggingEvent>();
        listAppender.setContext(lc);
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    public void testFormattingOneArg() {
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.debug("{}", new Integer(12));
        ILoggingEvent event = (ILoggingEvent) listAppender.list.get(0);
        assertEquals("12", event.getFormattedMessage());
    }

    @Test
    public void testFormattingTwoArg() {
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.debug("{}-{}", new Integer(12), new Integer(13));
        ILoggingEvent event = (ILoggingEvent) listAppender.list.get(0);
        assertEquals("12-13", event.getFormattedMessage());
    }

    @Test
    public void testNoFormatting() {
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.debug("test", new Integer(12), new Integer(13));
        ILoggingEvent event = (ILoggingEvent) listAppender.list.get(0);
        assertEquals("test", event.getFormattedMessage());
    }

    @Test
    public void testNoFormatting2() {
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.debug("test");
        ILoggingEvent event = (ILoggingEvent) listAppender.list.get(0);
        assertEquals("test", event.getFormattedMessage());
    }

    @Test
    public void testMessageConverter() {
        Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.debug("{}", 12);
        ILoggingEvent event = (ILoggingEvent) listAppender.list.get(0);
        PatternLayout layout = new PatternLayout();
        layout.setContext(lc);
        layout.setPattern("%m");
        layout.start();
        String formattedMessage = layout.doLayout(event);
        assertEquals("12", formattedMessage);
    }

}
