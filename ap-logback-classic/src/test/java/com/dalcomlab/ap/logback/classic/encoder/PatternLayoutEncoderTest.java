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
package com.dalcomlab.ap.logback.classic.encoder;

import com.dalcomlab.ap.logback.classic.Level;
import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.PatternLayout;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class PatternLayoutEncoderTest {

    PatternLayoutEncoder ple = new PatternLayoutEncoder();
    LoggerContext context = new LoggerContext();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Logger logger = context.getLogger(PatternLayoutEncoderTest.class);
    Charset utf8Charset = Charset.forName("UTF-8");

    @Before
    public void setUp() {
        ple.setPattern("%m");
        ple.setContext(context);
    }

    ILoggingEvent makeLoggingEvent(String message) {
        return new LoggingEvent("", logger, Level.DEBUG, message, null, null);
    }

    @Test
    public void smoke() throws IOException {
        init(baos);
        String msg = "hello";
        ILoggingEvent event = makeLoggingEvent(msg);
        byte[] eventBytes = ple.encode(event);
        baos.write(eventBytes);
        ple.footerBytes();
        assertEquals(msg, baos.toString());
    }

    void init(ByteArrayOutputStream baos) throws IOException {
        ple.start();
        ((PatternLayout) ple.getLayout()).setOutputPatternAsHeader(false);
        byte[] header = ple.headerBytes();
        baos.write(header);
    }

    @Test
    public void charset() throws IOException {
        ple.setCharset(utf8Charset);
        init(baos);
        String msg = "\u03b1";
        ILoggingEvent event = makeLoggingEvent(msg);
        byte[] eventBytes = ple.encode(event);
        baos.write(eventBytes);
        ple.footerBytes();
        assertEquals(msg, new String(baos.toByteArray(), utf8Charset));
    }

}
