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
package com.dalcomlab.ap.logback.classic.net.testObjectBuilders;

import com.dalcomlab.ap.logback.classic.Level;
import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.LoggingEvent;

public class LoggingEventBuilderInContext implements Builder<ILoggingEvent> {

    LoggerContext loggerContext;
    Logger logger;
    String fqcn;

    public LoggingEventBuilderInContext(LoggerContext loggerContext, String loggerName, String fqcn) {
        this.loggerContext = loggerContext;
        logger = loggerContext.getLogger(loggerName);
        this.fqcn = fqcn;
    }

    public ILoggingEvent build(int i) {
        LoggingEvent le = new LoggingEvent(fqcn, logger, Level.DEBUG, "hello " + i, null, null);
        return le;
    }

}
