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
package com.dalcomlab.ap.logback.classic.net.server;

import com.dalcomlab.ap.logback.core.net.mock.MockContext;
import com.dalcomlab.ap.logback.core.net.server.MockServerListener;
import com.dalcomlab.ap.logback.core.net.server.MockServerRunner;
import com.dalcomlab.ap.logback.core.net.server.ServerSocketUtil;
import com.dalcomlab.ap.logback.core.status.ErrorStatus;
import com.dalcomlab.ap.logback.core.status.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ServerSocketReceiver}.
 *
 * @author Carl Harris
 */
public class ServerSocketReceiverTest {

    private MockContext context = new MockContext();

    private MockServerRunner<RemoteAppenderClient> runner = new MockServerRunner<RemoteAppenderClient>();

    private MockServerListener<RemoteAppenderClient> listener = new MockServerListener<RemoteAppenderClient>();

    private ServerSocket serverSocket;
    private InstrumentedServerSocketReceiver receiver;

    @Before
    public void setUp() throws Exception {
        serverSocket = ServerSocketUtil.createServerSocket();
        receiver = new InstrumentedServerSocketReceiver(serverSocket, listener, runner);
        receiver.setContext(context);
    }

    @After
    public void tearDown() throws Exception {
        serverSocket.close();
    }

    @Test
    public void testStartStop() throws Exception {
        receiver.start();
        assertTrue(runner.isContextInjected());
        assertTrue(runner.isRunning());
        assertSame(listener, receiver.getLastListener());

        receiver.stop();
        assertFalse(runner.isRunning());
    }

    @Test
    public void testStartWhenAlreadyStarted() throws Exception {
        receiver.start();
        receiver.start();
        assertEquals(1, runner.getStartCount());
    }

    @Test
    public void testStopThrowsException() throws Exception {
        receiver.start();
        assertTrue(receiver.isStarted());
        IOException ex = new IOException("test exception");
        runner.setStopException(ex);
        receiver.stop();

        Status status = context.getLastStatus();
        assertNotNull(status);
        assertTrue(status instanceof ErrorStatus);
        assertTrue(status.getMessage().contains(ex.getMessage()));
        assertSame(ex, status.getThrowable());
    }

    @Test
    public void testStopWhenNotStarted() throws Exception {
        receiver.stop();
        assertEquals(0, runner.getStartCount());
    }

}
