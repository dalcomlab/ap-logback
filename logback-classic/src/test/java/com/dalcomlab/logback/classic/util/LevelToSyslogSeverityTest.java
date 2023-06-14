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
package com.dalcomlab.logback.classic.util;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import com.dalcomlab.logback.core.net.SyslogConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelToSyslogSeverityTest {

    @Test
    public void smoke() {

        assertEquals(SyslogConstants.DEBUG_SEVERITY, LevelToSyslogSeverity.convert(createEventOfLevel(Level.TRACE)));

        assertEquals(SyslogConstants.DEBUG_SEVERITY, LevelToSyslogSeverity.convert(createEventOfLevel(Level.DEBUG)));

        assertEquals(SyslogConstants.INFO_SEVERITY, LevelToSyslogSeverity.convert(createEventOfLevel(Level.INFO)));

        assertEquals(SyslogConstants.WARNING_SEVERITY, LevelToSyslogSeverity.convert(createEventOfLevel(Level.WARN)));

        assertEquals(SyslogConstants.ERROR_SEVERITY, LevelToSyslogSeverity.convert(createEventOfLevel(Level.ERROR)));

    }

    ILoggingEvent createEventOfLevel(Level level) {
        LoggingEvent event = new LoggingEvent();
        event.setLevel(level);
        return event;
    }

}
