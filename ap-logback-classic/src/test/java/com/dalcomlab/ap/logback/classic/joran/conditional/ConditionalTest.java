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
package com.dalcomlab.ap.logback.classic.joran.conditional;

import com.dalcomlab.ap.logback.classic.ClassicTestConstants;
import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.joran.JoranConfigurator;
import com.dalcomlab.ap.logback.core.ConsoleAppender;
import com.dalcomlab.ap.logback.core.FileAppender;
import com.dalcomlab.ap.logback.core.joran.spi.JoranException;
import com.dalcomlab.ap.logback.core.read.ListAppender;
import com.dalcomlab.ap.logback.core.status.StatusChecker;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import com.dalcomlab.ap.logback.core.util.CoreTestConstants;
import com.dalcomlab.ap.logback.core.util.StatusPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConditionalTest {

    LoggerContext context = new LoggerContext();
    Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);

    int diff = RandomUtil.getPositiveInt();
    String randomOutputDir = CoreTestConstants.OUTPUT_DIR_PREFIX + diff + "/";

    @Before
    public void setUp() throws UnknownHostException {
        context.setName("c" + diff);
        context.putProperty("randomOutputDir", randomOutputDir);
    }

    @After
    public void tearDown() {
        StatusPrinter.printIfErrorsOccured(context);
    }

    void configure(String file) throws JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        jc.doConfigure(file);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void conditionalConsoleApp_IF_THEN_True() throws JoranException, IOException, InterruptedException {
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("In conditionalConsoleApp_IF_THEN_True, canonicalHostName=\"" + localhost.getCanonicalHostName() + "] and hostNmae=\""
                        + localhost.getHostName() + "\"");
        context.putProperty("aHost", localhost.getHostName());

        String configFileAsStr = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/conditionalConsoleApp.xml";
        configure(configFileAsStr);
        FileAppender fileAppender = (FileAppender) root.getAppender("FILE");
        assertNotNull(fileAppender);

        ConsoleAppender consoleAppender = (ConsoleAppender) root.getAppender("CON");
        assertNotNull(consoleAppender);
        StatusChecker checker = new StatusChecker(context);
        checker.assertIsErrorFree();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void conditionalConsoleApp_IF_THEN_False() throws JoranException, IOException, InterruptedException {

        String configFileAsStr = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/conditionalConsoleApp.xml";
        configure(configFileAsStr);
        FileAppender fileAppender = (FileAppender) root.getAppender("FILE");
        assertNotNull(fileAppender);

        ConsoleAppender consoleAppender = (ConsoleAppender) root.getAppender("CON");
        assertNull(consoleAppender);
        StatusChecker checker = new StatusChecker(context);
        checker.assertIsErrorFree();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void conditionalConsoleApp_IF_THEN_ELSE() throws JoranException, IOException, InterruptedException {

        String configFileAsStr = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/conditionalConsoleApp_ELSE.xml";
        configure(configFileAsStr);

        FileAppender fileAppender = (FileAppender) root.getAppender("FILE");
        assertNotNull(fileAppender);

        ConsoleAppender consoleAppender = (ConsoleAppender) root.getAppender("CON");
        assertNull(consoleAppender);

        ListAppender listAppender = (ListAppender) root.getAppender("LIST");
        assertNotNull(listAppender);

        // StatusPrinter.printIfErrorsOccured(context);
        StatusChecker checker = new StatusChecker(context);
        checker.assertIsErrorFree();
    }

    @Test
    public void conditionalInclusionWithExistingFile() throws JoranException, IOException, InterruptedException {

        String configFileAsStr = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/conditionalIncludeExistingFile.xml";
        configure(configFileAsStr);

        ConsoleAppender consoleAppender = (ConsoleAppender) root.getAppender("CON");
        assertNotNull(consoleAppender);
        StatusChecker checker = new StatusChecker(context);
        checker.assertIsErrorFree();
    }

    @Test
    public void conditionalInclusionWithInexistentFile() throws JoranException, IOException, InterruptedException {

        String configFileAsStr = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/conditionalIncludeInexistentFile.xml";
        configure(configFileAsStr);

        ConsoleAppender consoleAppender = (ConsoleAppender) root.getAppender("CON");
        assertNull(consoleAppender);
        StatusChecker checker = new StatusChecker(context);
        checker.assertIsErrorFree();
    }

}
