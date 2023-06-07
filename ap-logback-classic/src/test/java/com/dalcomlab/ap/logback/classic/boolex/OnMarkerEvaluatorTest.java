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
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.boolex.OnMarkerEvaluator;
import com.dalcomlab.ap.logback.classic.spi.LoggingEvent;
import com.dalcomlab.ap.logback.core.boolex.EvaluationException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MarkerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OnMarkerEvaluatorTest {

    LoggerContext lc = new LoggerContext();
    LoggingEvent event = makeEvent();
    OnMarkerEvaluator evaluator = new OnMarkerEvaluator();

    @Before
    public void before() {
        evaluator.setContext(lc);
    }

    @Test
    public void smoke() throws EvaluationException {
        evaluator.addMarker("M");
        evaluator.start();

        event.setMarker(MarkerFactory.getMarker("M"));
        assertTrue(evaluator.evaluate(event));
    }

    @Test
    public void nullMarkerInEvent() throws EvaluationException {
        evaluator.addMarker("M");
        evaluator.start();
        assertFalse(evaluator.evaluate(event));
    }

    @Test
    public void nullMarkerInEvaluator() throws EvaluationException {
        evaluator.addMarker("M");
        evaluator.start();
        assertFalse(evaluator.evaluate(event));
    }

    LoggingEvent makeEvent() {
        return new LoggingEvent("x", lc.getLogger("x"), Level.DEBUG, "msg", null, null);
    }
}
