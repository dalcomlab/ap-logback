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
import com.dalcomlab.ap.logback.classic.spi.LoggingEvent;

public class TrivialLoggingEventBuilder implements Builder {

    LoggerContext loggerContext = new LoggerContext();

    private Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

    public Object build(int i) {
        LoggingEvent le = new LoggingEvent();
        le.setTimeStamp(System.currentTimeMillis());
        le.setLevel(Level.DEBUG);
        le.setLoggerName(logger.getName());
        le.setLoggerContextRemoteView(loggerContext.getLoggerContextRemoteView());
        le.setMessage(MSG_PREFIX);
        le.setThreadName("threadName");
        return le;
    }
}
