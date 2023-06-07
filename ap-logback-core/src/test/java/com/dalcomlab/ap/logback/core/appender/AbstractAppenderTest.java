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
package com.dalcomlab.ap.logback.core.appender;

import com.dalcomlab.ap.logback.core.Appender;
import com.dalcomlab.ap.logback.core.Context;
import com.dalcomlab.ap.logback.core.ContextBase;
import com.dalcomlab.ap.logback.core.status.StatusChecker;
import com.dalcomlab.ap.logback.core.util.StatusPrinter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

abstract public class AbstractAppenderTest<E> {

    abstract protected Appender<E> getAppender();

    abstract protected Appender<E> getConfiguredAppender();

    Context context = new ContextBase();

    @Test
    public void testNewAppender() {
        // new appenders should be inactive
        Appender<E> appender = getAppender();
        assertFalse(appender.isStarted());
    }

    @Test
    public void testConfiguredAppender() {
        Appender<E> appender = getConfiguredAppender();
        appender.start();
        assertTrue(appender.isStarted());

        appender.stop();
        assertFalse(appender.isStarted());

    }

    @Test
    public void testNoStart() {
        Appender<E> appender = getAppender();
        appender.setContext(context);
        appender.setName("doh");
        // is null OK?
        appender.doAppend(null);
        StatusChecker checker = new StatusChecker(context.getStatusManager());
        StatusPrinter.print(context);
        checker.assertContainsMatch("Attempted to append to non started appender \\[doh\\].");
    }
}
