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
package com.dalcomlab.ap.logback.core.net.server;

import com.dalcomlab.ap.logback.core.net.mock.MockContext;
import com.dalcomlab.ap.logback.core.spi.PreSerializationTransformer;
import com.dalcomlab.ap.logback.core.util.ExecutorServiceUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link SSLServerSocketAppenderBase}.
 *
 * @author Carl Harris
 */
public class SSLServerSocketAppenderBaseTest {

    private MockContext context = new MockContext(ExecutorServiceUtil.newScheduledExecutorService());

    private SSLServerSocketAppenderBase appender = new InstrumentedSSLServerSocketAppenderBase();

    @Before
    public void setUp() throws Exception {
        appender.setContext(context);
    }

    @Test
    public void testUsingDefaultConfig() throws Exception {
        // should be able to start successfully with no SSL configuration at all
        appender.start();
        assertNotNull(appender.getServerSocketFactory());
        appender.stop();
    }

    private static class InstrumentedSSLServerSocketAppenderBase extends SSLServerSocketAppenderBase<Object> {

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
