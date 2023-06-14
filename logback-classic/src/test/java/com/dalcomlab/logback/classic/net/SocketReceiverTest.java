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
package com.dalcomlab.logback.classic.net;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.net.mock.MockAppender;
import com.dalcomlab.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEvent;
import com.dalcomlab.logback.classic.spi.LoggingEventVO;
import com.dalcomlab.logback.core.net.SocketConnector;
import com.dalcomlab.logback.core.net.server.ServerSocketUtil;
import com.dalcomlab.logback.core.status.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SocketReceiver}.
 *
 * @author Carl Harris
 */
public class SocketReceiverTest {

    private static final int DELAY = 1000;
    private static final String TEST_HOST_NAME = "NOT.A.VALID.HOST.NAME";

    private ServerSocket serverSocket;
    private Socket socket;
    private MockSocketFactory socketFactory = new MockSocketFactory();
    private MockSocketConnector connector;
    private MockAppender appender;
    private LoggerContext lc;
    private Logger logger;

    private InstrumentedSocketReceiver receiver = new InstrumentedSocketReceiver();

    @Before
    public void setUp() throws Exception {
        serverSocket = ServerSocketUtil.createServerSocket();
        socket = new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort());
        connector = new MockSocketConnector(socket);

        lc = new LoggerContext();
        lc.reset();
        receiver.setContext(lc);
        appender = new MockAppender();
        appender.start();
        logger = lc.getLogger(getClass());
        logger.addAppender(appender);
    }

    @After
    public void tearDown() throws Exception {
        receiver.stop();
        ExecutorService executor = lc.getExecutorService();
        executor.shutdownNow();
        assertTrue(executor.awaitTermination(DELAY, TimeUnit.MILLISECONDS));
        socket.close();
        serverSocket.close();
        lc.stop();
    }

    @Test
    public void testStartNoRemoteAddress() throws Exception {
        receiver.start();
        assertFalse(receiver.isStarted());
        int count = lc.getStatusManager().getCount();
        Status status = lc.getStatusManager().getCopyOfStatusList().get(count - 1);
        assertTrue(status.getMessage().contains("host"));
    }

    @Test
    public void testStartNoPort() throws Exception {
        receiver.setRemoteHost(TEST_HOST_NAME);
        receiver.start();
        assertFalse(receiver.isStarted());
        int count = lc.getStatusManager().getCount();
        Status status = lc.getStatusManager().getCopyOfStatusList().get(count - 1);
        assertTrue(status.getMessage().contains("port"));
    }

    @Test
    public void testStartUnknownHost() throws Exception {
        receiver.setPort(6000);
        receiver.setRemoteHost(TEST_HOST_NAME);
        receiver.start();
        assertFalse(receiver.isStarted());
        int count = lc.getStatusManager().getCount();
        Status status = lc.getStatusManager().getCopyOfStatusList().get(count - 1);
        assertTrue(status.getMessage().contains("unknown host"));
    }

    @Test
    public void testStartStop() throws Exception {
        receiver.setRemoteHost(InetAddress.getLocalHost().getHostName());
        receiver.setPort(6000);
        receiver.setAcceptConnectionTimeout(DELAY / 2);
        receiver.start();
        assertTrue(receiver.isStarted());
        receiver.awaitConnectorCreated(DELAY);
        receiver.stop();
        assertFalse(receiver.isStarted());
    }

    @Test
    public void testServerSlowToAcceptConnection() throws Exception {
        receiver.setRemoteHost(InetAddress.getLocalHost().getHostName());
        receiver.setPort(6000);
        receiver.setAcceptConnectionTimeout(DELAY / 4);
        receiver.start();
        assertTrue(receiver.awaitConnectorCreated(DELAY / 2));
        // note that we don't call serverSocket.accept() here
        // but processPriorToRemoval (in tearDown) should still clean up everything
    }

    @Test
    public void testServerDropsConnection() throws Exception {
        receiver.setRemoteHost(InetAddress.getLocalHost().getHostName());
        receiver.setPort(6000);
        receiver.start();
        assertTrue(receiver.awaitConnectorCreated(DELAY));
        Socket socket = serverSocket.accept();
        socket.close();
    }

    @Test
    public void testDispatchEventForEnabledLevel() throws Exception {
        receiver.setRemoteHost(InetAddress.getLocalHost().getHostName());
        receiver.setPort(6000);
        receiver.start();
        assertTrue(receiver.awaitConnectorCreated(DELAY));
        Socket socket = serverSocket.accept();

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        logger.setLevel(Level.DEBUG);
        ILoggingEvent event = new LoggingEvent(logger.getName(), logger, Level.DEBUG, "test message", null, new Object[0]);

        LoggingEventVO eventVO = LoggingEventVO.build(event);
        oos.writeObject(eventVO);
        oos.flush();

        ILoggingEvent rcvdEvent = appender.awaitAppend(DELAY);
        assertNotNull(rcvdEvent);
        assertEquals(event.getLoggerName(), rcvdEvent.getLoggerName());
        assertEquals(event.getLevel(), rcvdEvent.getLevel());
        assertEquals(event.getMessage(), rcvdEvent.getMessage());
    }

    @Test
    public void testNoDispatchEventForDisabledLevel() throws Exception {
        receiver.setRemoteHost(InetAddress.getLocalHost().getHostName());
        receiver.setPort(6000);
        receiver.start();
        assertTrue(receiver.awaitConnectorCreated(DELAY));
        Socket socket = serverSocket.accept();

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        logger.setLevel(Level.INFO);
        ILoggingEvent event = new LoggingEvent(logger.getName(), logger, Level.DEBUG, "test message", null, new Object[0]);

        LoggingEventVO eventVO = LoggingEventVO.build(event);
        oos.writeObject(eventVO);
        oos.flush();

        assertNull(appender.awaitAppend(DELAY));
    }

    /**
     * A {@link SocketReceiver} with instrumentation for unit testing.
     */
    private class InstrumentedSocketReceiver extends SocketReceiver {

        private boolean connectorCreated;

        @Override
        protected synchronized SocketConnector newConnector(InetAddress address, int port, int initialDelay, int retryDelay) {
            connectorCreated = true;
            notifyAll();
            return connector;
        }

        @Override
        protected SocketFactory getSocketFactory() {
            return socketFactory;
        }

        public synchronized boolean awaitConnectorCreated(long delay) throws InterruptedException {
            while (!connectorCreated) {
                wait(delay);
            }
            return connectorCreated;
        }

    }

    /**
     * A {@link SocketConnector} with instrumentation for unit testing.
     */
    private static class MockSocketConnector implements SocketConnector {

        private final Socket socket;

        public MockSocketConnector(Socket socket) {
            this.socket = socket;
        }

        public Socket call() throws InterruptedException {
            return socket;
        }

        public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        }

        public void setSocketFactory(SocketFactory socketFactory) {
        }

    }

    /**
     * A no-op {@link SocketFactory} to support unit testing.
     */
    private static class MockSocketFactory extends SocketFactory {

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            throw new UnsupportedOperationException();
        }

    }

}
