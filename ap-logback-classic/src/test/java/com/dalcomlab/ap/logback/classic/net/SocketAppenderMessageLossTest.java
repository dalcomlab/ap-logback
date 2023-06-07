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
package com.dalcomlab.ap.logback.classic.net;

import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.net.SimpleSocketServer;
import com.dalcomlab.ap.logback.classic.net.SocketAppender;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.AppenderBase;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import com.dalcomlab.ap.logback.core.util.Duration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertTrue;

public class SocketAppenderMessageLossTest {
    int runLen = 100;
    Duration reconnectionDelay = new Duration(1000);

    static final int TIMEOUT = 3000;

    @Test  // (timeout = TIMEOUT)
    public void synchronousSocketAppender() throws Exception {

        SocketAppender socketAppender = new SocketAppender();
        socketAppender.setReconnectionDelay(reconnectionDelay);
        socketAppender.setIncludeCallerData(true);

        runTest(socketAppender);
    }

    @Test(timeout = TIMEOUT)
    public void smallQueueSocketAppender() throws Exception {

        SocketAppender socketAppender = new SocketAppender();
        socketAppender.setReconnectionDelay(reconnectionDelay);
        socketAppender.setQueueSize(runLen / 10);

        runTest(socketAppender);
    }

    @Test(timeout = TIMEOUT)
    public void largeQueueSocketAppender() throws Exception {
        SocketAppender socketAppender = new SocketAppender();
        socketAppender.setReconnectionDelay(reconnectionDelay);
        socketAppender.setQueueSize(runLen * 5);

        runTest(socketAppender);
    }

    // appender used to signal when the N'th event (as set in the latch) is received by the server
    // this allows us to have test which are both more robust and quicker.
    static public class ListAppenderWithLatch extends AppenderBase<ILoggingEvent> {
        public List<ILoggingEvent> list = new ArrayList<ILoggingEvent>();
        CountDownLatch latch;

        ListAppenderWithLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        protected void append(ILoggingEvent e) {
            list.add(e);
            latch.countDown();
        }
    }

    public void runTest(SocketAppender socketAppender) throws Exception {
        final int port = RandomUtil.getRandomServerPort();

        LoggerContext serverLoggerContext = new LoggerContext();
        serverLoggerContext.setName("serverLoggerContext");

        CountDownLatch allMessagesReceivedLatch = new CountDownLatch(runLen);
        ListAppenderWithLatch listAppender = new ListAppenderWithLatch(allMessagesReceivedLatch);
        listAppender.setContext(serverLoggerContext);
        listAppender.start();

        Logger serverRootLogger = serverLoggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        serverRootLogger.setAdditive(false);
        serverRootLogger.addAppender(listAppender);

        LoggerContext loggerContext = new LoggerContext();
        loggerContext.setName("clientLoggerContext");
        socketAppender.setContext(loggerContext);

        CountDownLatch latch = new CountDownLatch(1);
        SimpleSocketServer simpleSocketServer = new SimpleSocketServer(serverLoggerContext, port);
        simpleSocketServer.start();
        simpleSocketServer.setLatch(latch);

        latch.await();

        socketAppender.setPort(port);
        socketAppender.setRemoteHost("localhost");
        socketAppender.setReconnectionDelay(reconnectionDelay);
        socketAppender.start();
        assertTrue(socketAppender.isStarted());

        Logger logger = loggerContext.getLogger(getClass());
        logger.setAdditive(false);
        logger.addAppender(socketAppender);

        for (int i = 0; i < runLen; ++i) {
            logger.info("hello");
        }

        allMessagesReceivedLatch.await();
        loggerContext.stop();
        simpleSocketServer.close();

 
    }
}
