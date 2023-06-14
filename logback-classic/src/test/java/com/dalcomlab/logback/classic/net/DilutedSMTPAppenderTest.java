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
package com.dalcomlab.logback.classic.net;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.PatternLayout;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import com.dalcomlab.logback.core.Layout;
import com.dalcomlab.logback.core.helpers.CyclicBuffer;
import com.dalcomlab.logback.core.spi.CyclicBufferTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DilutedSMTPAppenderTest {

    SMTPAppender appender;
    CyclicBufferTracker<ILoggingEvent> cbTracker;
    CyclicBuffer<ILoggingEvent> cb;

    @Before
    public void setUp() throws Exception {
        LoggerContext lc = new LoggerContext();
        appender = new SMTPAppender();
        appender.setContext(lc);
        appender.setName("smtp");
        appender.setFrom("user@host.dom");
        appender.setLayout(buildLayout(lc));
        appender.setSMTPHost("mail2.qos.ch");
        appender.setSubject("logging report");
        appender.addTo("sebastien.nospam@qos.ch");
        appender.start();
        cbTracker = appender.getCyclicBufferTracker();
        cb = cbTracker.getOrCreate("", 0);

    }

    private static Layout<ILoggingEvent> buildLayout(LoggerContext lc) {
        PatternLayout layout = new PatternLayout();
        layout.setContext(lc);
        layout.setFileHeader("Some header\n");
        layout.setPattern("%-4relative [%thread] %-5level %class - %msg%n");
        layout.setFileFooter("Some footer");
        layout.start();
        return layout;
    }

    @After
    public void tearDown() throws Exception {
        appender = null;
    }

    @Test
    public void testStart() {
        assertEquals("sebastien.nospam@qos.ch%nopex", appender.getToAsListOfString().get(0));

        assertEquals("logging report", appender.getSubject());

        assertTrue(appender.isStarted());
    }

    @Test
    public void testAppendNonTriggeringEvent() {
        LoggingEvent event = new LoggingEvent();
        event.setThreadName("thead name");
        event.setLevel(Level.DEBUG);
        appender.subAppend(cb, event);
        assertEquals(1, cb.length());
    }

    @Test
    public void testEntryConditionsCheck() {
        appender.checkEntryConditions();
        assertEquals(0, appender.getContext().getStatusManager().getCount());
    }

    @Test
    public void testTriggeringPolicy() {
        appender.setEvaluator(null);
        appender.checkEntryConditions();
        assertEquals(1, appender.getContext().getStatusManager().getCount());
    }

    @Test
    public void testEntryConditionsCheckNoLayout() {
        appender.setLayout(null);
        appender.checkEntryConditions();
        assertEquals(1, appender.getContext().getStatusManager().getCount());
    }

}
