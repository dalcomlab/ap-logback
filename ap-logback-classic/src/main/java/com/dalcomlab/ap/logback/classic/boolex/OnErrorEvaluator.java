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
package com.dalcomlab.ap.logback.classic.boolex;

import com.dalcomlab.ap.logback.classic.Level;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.boolex.EvaluationException;
import com.dalcomlab.ap.logback.core.boolex.EventEvaluatorBase;

/**
 * Evaluates to true when the logging event passed as parameter has level ERROR
 * or higher.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class OnErrorEvaluator extends EventEvaluatorBase<ILoggingEvent> {

    /**
     * Return true if event passed as parameter has level ERROR or higher, returns
     * false otherwise.
     */
    public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
        return event.getLevel().levelInt >= Level.ERROR_INT;
    }
}
