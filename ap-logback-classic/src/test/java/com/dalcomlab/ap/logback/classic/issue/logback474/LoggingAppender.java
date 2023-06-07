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
package com.dalcomlab.ap.logback.classic.issue.logback474;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.AppenderBase;
import org.slf4j.Logger;

/**
 * An appender which calls logback recursively
 * 
 * @author Ralph Goers
 */

public class LoggingAppender extends AppenderBase<ILoggingEvent> {

    Logger logger;

    public void start() {
        super.start();
        logger = ((LoggerContext) getContext()).getLogger("Ignore");
    }

    protected void append(ILoggingEvent eventObject) {
        logger.debug("Ignore this");
    }
}
