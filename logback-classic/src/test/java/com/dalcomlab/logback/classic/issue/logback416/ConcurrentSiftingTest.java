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
package com.dalcomlab.logback.classic.issue.logback416;

import com.dalcomlab.logback.classic.ClassicTestConstants;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.issue.lbclassic135.LoggingRunnable;
import com.dalcomlab.logback.classic.joran.JoranConfigurator;
import com.dalcomlab.logback.core.contention.MultiThreadedHarness;
import com.dalcomlab.logback.core.contention.RunnableWithCounterAndDone;
import com.dalcomlab.logback.core.joran.spi.JoranException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConcurrentSiftingTest {
    final static int THREAD_COUNT = 5;
    static String FOLDER_PREFIX = ClassicTestConstants.JORAN_INPUT_PREFIX + "sift/";

    LoggerContext loggerContext = new LoggerContext();
    protected Logger logger = loggerContext.getLogger(this.getClass().getName());
    protected Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

    int totalTestDuration = 50;
    MultiThreadedHarness harness = new MultiThreadedHarness(totalTestDuration);
    RunnableWithCounterAndDone[] runnableArray = buildRunnableArray();

    protected void configure(String file) throws JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        jc.doConfigure(file);
    }

    RunnableWithCounterAndDone[] buildRunnableArray() {
        RunnableWithCounterAndDone[] rArray = new RunnableWithCounterAndDone[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            rArray[i] = new LoggingRunnable(logger);
        }
        return rArray;
    }

    @Test
    public void concurrentAccess() throws JoranException, InterruptedException {
        configure(FOLDER_PREFIX + "logback_416.xml");
        harness.execute(runnableArray);
        assertEquals(1, InstanceCountingAppender.INSTANCE_COUNT.get());
    }
}
