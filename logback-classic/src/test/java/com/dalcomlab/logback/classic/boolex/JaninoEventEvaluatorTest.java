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
package com.dalcomlab.logback.classic.boolex;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.pattern.ConverterTest;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import com.dalcomlab.logback.core.boolex.EvaluationException;
import com.dalcomlab.logback.core.boolex.JaninoEventEvaluatorBase;
import com.dalcomlab.logback.core.boolex.Matcher;
import com.dalcomlab.logback.core.filter.EvaluatorFilter;
import com.dalcomlab.logback.core.spi.FilterReply;
import com.dalcomlab.logback.core.testUtil.RandomUtil;
import com.dalcomlab.logback.core.util.StatusPrinter;
import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import java.io.IOException;

import static org.junit.Assert.*;

public class JaninoEventEvaluatorTest {

    LoggerContext loggerContext = new LoggerContext();
    Logger logger = loggerContext.getLogger(ConverterTest.class);

    Matcher matcherX = new Matcher();

    JaninoEventEvaluator jee = new JaninoEventEvaluator();

    int diff = RandomUtil.getPositiveInt();

    public JaninoEventEvaluatorTest() {
        jee.setContext(loggerContext);

        matcherX.setName("x");
        matcherX.setRegex("^Some\\s.*");
        matcherX.start();

    }

    LoggingEvent makeLoggingEvent(Exception ex) {
        return new LoggingEvent(com.dalcomlab.logback.core.pattern.FormattingConverter.class.getName(), logger, Level.INFO, "Some message", ex, null);
    }

    @Test
    public void testBasic() throws Exception {
        jee.setExpression("message.equals(\"Some message\")");
        jee.start();

        StatusPrinter.print(loggerContext);
        ILoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void testLevel() throws Exception {
        jee.setExpression("level > DEBUG");
        jee.start();

        ILoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void testtimeStamp() throws Exception {
        jee.setExpression("timeStamp > 10");
        jee.start();

        ILoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void testWithMatcher() throws Exception {
        jee.setExpression("x.matches(message)");
        jee.addMatcher(matcherX);
        jee.start();

        ILoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void mdcAsString() throws Exception {
        String k = "key" + diff;

        MDC.put("key" + diff, "value" + diff);
        jee.setExpression("((String) mdc.get(\"" + k + "\")).contains(\"alue\")");
        jee.start();
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);

        LoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
        MDC.remove(k);
    }

    @Test
    public void marker() throws Exception {
        jee.setExpression("marker.contains(\"BLUE\")");
        jee.start();

        LoggingEvent event = makeLoggingEvent(null);
        event.setMarker(MarkerFactory.getMarker("BLUE"));
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void withNullMarker_LBCORE_118() throws Exception {
        jee.setExpression("marker.contains(\"BLUE\")");
        jee.start();

        ILoggingEvent event = makeLoggingEvent(null);
        try {
            jee.evaluate(event);
            fail("We should not reach this point");
        } catch (EvaluationException ee) {
            // received an exception as expected
        }
    }

    @Test
    public void evaluatorFilterWithNullMarker_LBCORE_118() throws Exception {
        EvaluatorFilter<ILoggingEvent> ef = new EvaluatorFilter<ILoggingEvent>();
        ef.setContext(loggerContext);

        ef.setOnMatch(FilterReply.ACCEPT);
        ef.setOnMismatch(FilterReply.DENY);

        jee.setExpression("marker.contains(\"BLUE\")");
        jee.start();

        ef.setEvaluator(jee);
        ef.start();
        ILoggingEvent event = makeLoggingEvent(null);
        assertEquals(FilterReply.NEUTRAL, ef.decide(event));

    }

    @Test
    public void testComplex() throws Exception {
        jee.setExpression("level >= INFO && x.matches(message) && marker.contains(\"BLUE\")");
        jee.addMatcher(matcherX);
        jee.start();

        LoggingEvent event = makeLoggingEvent(null);
        event.setMarker(MarkerFactory.getMarker("BLUE"));
        assertTrue(jee.evaluate(event));
    }

    /**
     * check that evaluator with bogus exp does not start
     * 
     * @throws Exception
     */
    @Test
    public void testBogusExp1() {
        jee.setExpression("mzzzz.get(\"key\").equals(null)");
        jee.setName("bogus");
        jee.start();

        assertFalse(jee.isStarted());
    }

    // check that eval stops after errors
    @Test
    public void testBogusExp2() {
        jee.setExpression("mdc.get(\"keyXN89\").equals(null)");
        jee.setName("bogus");
        jee.start();

        assertTrue(jee.isStarted());

        ILoggingEvent event = makeLoggingEvent(null);

        for (int i = 0; i < JaninoEventEvaluatorBase.ERROR_THRESHOLD; i++) {
            try {
                jee.evaluate(event);
                fail("should throw an exception");
            } catch (EvaluationException e) {
            }
        }
        // after a few attempts the evaluator should processPriorToRemoval
        assertFalse(jee.isStarted());

    }

    static final long LEN = 10 * 1000;

    // with 6 parameters 400 nanos
    // with 7 parameters 460 nanos (all levels + selected fields from
    // LoggingEvent)
    // with 10 parameters 510 nanos (all levels + fields)
    void loop(JaninoEventEvaluator jee, String msg) throws Exception {
        ILoggingEvent event = makeLoggingEvent(null);
        // final long start = System.nanoTime();
        for (int i = 0; i < LEN; i++) {
            jee.evaluate(event);
        }
        // final long end = System.nanoTime();
        // System.out.println(msg + (end - start) / LEN + " nanos");
    }

    @Test
    public void testLoop1() throws Exception {
        jee.setExpression("timeStamp > 10");
        jee.start();

        loop(jee, "timestamp > 10]: ");
    }

    @Test
    public void testLoop2() throws Exception {
        jee.setExpression("x.matches(message)");
        jee.addMatcher(matcherX);
        jee.start();

        loop(jee, "x.matches(message): ");
    }

    @Test
    public void throwable_LBCLASSIC_155_I() throws EvaluationException {
        jee.setExpression("throwable instanceof java.io.IOException");
        jee.start();

        LoggingEvent event = makeLoggingEvent(new IOException(""));
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void throwable_LBCLASSIC_155_II() throws EvaluationException {
        jee.setExpression("throwableProxy.getClassName().contains(\"IO\")");
        jee.start();

        LoggingEvent event = makeLoggingEvent(new IOException(""));
        assertTrue(jee.evaluate(event));
    }

    @Test
    public void nullMDC() throws EvaluationException {
        MDC.clear();
        jee.setExpression("mdc.isEmpty()");
        jee.start();

        LoggingEvent event = makeLoggingEvent(null);
        assertTrue(jee.evaluate(event));
    }
}
