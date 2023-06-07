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
package com.dalcomlab.ap.logback.classic.util;

import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.util.ContextInitializer;
import com.dalcomlab.ap.logback.core.Appender;
import com.dalcomlab.ap.logback.core.ConsoleAppender;
import com.dalcomlab.ap.logback.core.CoreConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContextInitializerAutoConfigTest {

    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    Logger root = (Logger) LoggerFactory.getLogger("root");

    @Before
    public void setUp() throws Exception {
        logger.debug("Hello-didily-odily");
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(ContextInitializer.CONFIG_FILE_PROPERTY);
        System.clearProperty(CoreConstants.STATUS_LISTENER_CLASS_KEY);
    }

    @Test
    @Ignore
    // this test works only if logback-test.xml or logback.xml files are on the classpath.
    // However, this is something we try to avoid in order to simplify the life
    // of users trying to follows the manual and logback-examples from an IDE
    public void autoconfig() {
        LoggerContext iLoggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        iLoggerFactory.reset();
        Appender<ILoggingEvent> appender = root.getAppender("STDOUT");
        assertNotNull(appender);
        assertTrue(appender instanceof ConsoleAppender);
    }
}
