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
package com.dalcomlab.ap.logback.classic.net;

import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.LoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.LoggingEventVO;
import com.dalcomlab.ap.logback.core.spi.PreSerializationTransformer;

import java.io.Serializable;

public class LoggingEventPreSerializationTransformer implements PreSerializationTransformer<ILoggingEvent> {

    public Serializable transform(ILoggingEvent event) {
        if (event == null) {
            return null;
        }
        if (event instanceof LoggingEvent) {
            return LoggingEventVO.build(event);
        } else if (event instanceof LoggingEventVO) {
            return (LoggingEventVO) event;
        } else {
            throw new IllegalArgumentException("Unsupported type " + event.getClass().getName());
        }
    }

}
