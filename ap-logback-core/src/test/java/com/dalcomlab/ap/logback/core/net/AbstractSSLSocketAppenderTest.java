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
package com.dalcomlab.ap.logback.core.net;

import com.dalcomlab.ap.logback.core.net.mock.MockContext;
import com.dalcomlab.ap.logback.core.spi.PreSerializationTransformer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link AbstractSSLSocketAppender}.
 *
 * @author Carl Harris
 */
public class AbstractSSLSocketAppenderTest {

    private MockContext context = new MockContext();

    private InstrumentedSSLSocketAppenderBase appender = new InstrumentedSSLSocketAppenderBase();

    @Before
    public void setUp() throws Exception {
        appender.setContext(context);
    }

    @Test
    public void testUsingDefaultConfig() throws Exception {
        // should be able to start and stop successfully with no SSL
        // configuration at all
        appender.start();
        assertNotNull(appender.getSocketFactory());
        appender.stop();
    }

    private static class InstrumentedSSLSocketAppenderBase extends AbstractSSLSocketAppender<Object> {

        @Override
        protected void postProcessEvent(Object event) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected PreSerializationTransformer<Object> getPST() {
            throw new UnsupportedOperationException();
        }

    }
}
