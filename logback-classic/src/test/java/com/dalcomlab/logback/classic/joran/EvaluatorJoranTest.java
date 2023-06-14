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
package com.dalcomlab.logback.classic.joran;

import com.dalcomlab.logback.classic.ClassicTestConstants;
import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.boolex.JaninoEventEvaluator;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import com.dalcomlab.logback.core.CoreConstants;
import com.dalcomlab.logback.core.boolex.EvaluationException;
import com.dalcomlab.logback.core.boolex.EventEvaluator;
import com.dalcomlab.logback.core.joran.spi.JoranException;
import org.junit.Test;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Map;

import static org.junit.Assert.*;

public class EvaluatorJoranTest {

    @Test
    public void testSimpleEvaluator() throws NullPointerException, EvaluationException, JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        LoggerContext loggerContext = new LoggerContext();
        jc.setContext(loggerContext);
        jc.doConfigure(ClassicTestConstants.JORAN_INPUT_PREFIX + "simpleEvaluator.xml");

        @SuppressWarnings("unchecked")
        Map<String, EventEvaluator<?>> evalMap = (Map<String, EventEvaluator<?>>) loggerContext.getObject(CoreConstants.EVALUATOR_MAP);
        assertNotNull(evalMap);
        JaninoEventEvaluator evaluator = (JaninoEventEvaluator) evalMap.get("msgEval");
        assertNotNull(evaluator);

        Logger logger = loggerContext.getLogger("xx");
        ILoggingEvent event0 = new LoggingEvent("foo", logger, Level.DEBUG, "Hello world", null, null);
        assertTrue(evaluator.evaluate(event0));

        ILoggingEvent event1 = new LoggingEvent("foo", logger, Level.DEBUG, "random blurb", null, null);
        assertFalse(evaluator.evaluate(event1));
    }

    @Test
    public void testIgnoreMarker() throws NullPointerException, EvaluationException, JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        LoggerContext loggerContext = new LoggerContext();
        jc.setContext(loggerContext);
        jc.doConfigure(ClassicTestConstants.JORAN_INPUT_PREFIX + "ignore.xml");

        @SuppressWarnings("unchecked")
        Map<String, EventEvaluator<?>> evalMap = (Map<String, EventEvaluator<?>>) loggerContext.getObject(CoreConstants.EVALUATOR_MAP);
        assertNotNull(evalMap);

        Logger logger = loggerContext.getLogger("xx");

        JaninoEventEvaluator evaluator = (JaninoEventEvaluator) evalMap.get("IGNORE_EVAL");
        LoggingEvent event = new LoggingEvent("foo", logger, Level.DEBUG, "Hello world", null, null);

        Marker ignoreMarker = MarkerFactory.getMarker("IGNORE");
        event.setMarker(ignoreMarker);
        assertTrue(evaluator.evaluate(event));

        logger.debug("hello", new Exception("test"));
        logger.debug(ignoreMarker, "hello ignore", new Exception("test"));

        // logger.debug("hello", new Exception("test"));

        // StatusPrinter.print(loggerContext.getStatusManager());
    }

    @Test
    public void testMultipleConditionsInExpression() throws NullPointerException, EvaluationException {
        LoggerContext loggerContext = new LoggerContext();
        Logger logger = loggerContext.getLogger("xx");
        JaninoEventEvaluator ee = new JaninoEventEvaluator();
        ee.setName("testEval");
        ee.setContext(loggerContext);
        // &#38;&#38;
        // &amp;&amp;
        ee.setExpression("message.contains(\"stacktrace\") && message.contains(\"logging\")");
        ee.start();
        // StatusPrinter.print(loggerContext);

        String message = "stacktrace bla bla logging";
        ILoggingEvent event = new LoggingEvent(this.getClass().getName(), logger, Level.DEBUG, message, null, null);

        assertTrue(ee.evaluate(event));
    }
}
