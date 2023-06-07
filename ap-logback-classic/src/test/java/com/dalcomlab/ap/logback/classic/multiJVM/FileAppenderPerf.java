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
package com.dalcomlab.ap.logback.classic.multiJVM;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.encoder.PatternLayoutEncoder;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.FileAppender;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import org.slf4j.Logger;

public class FileAppenderPerf {
    static String msgLong = "ABCDEGHIJKLMNOPQRSTUVWXYZabcdeghijklmnopqrstuvwxyz1234567890";

    static long LEN = 100 * 1000;
    static int DIFF = RandomUtil.getPositiveInt() % 1000;
    static String FILENAME;

    static LoggerContext buildLoggerContext(String filename, boolean safetyMode) {
        LoggerContext loggerContext = new LoggerContext();

        FileAppender<ILoggingEvent> fa = new FileAppender<ILoggingEvent>();

        PatternLayoutEncoder patternLayout = new PatternLayoutEncoder();
        patternLayout.setPattern("%5p %c - %m%n");
        patternLayout.setContext(loggerContext);
        patternLayout.start();

        fa.setEncoder(patternLayout);
        fa.setFile(filename);
        fa.setAppend(false);
        fa.setPrudent(safetyMode);
        fa.setContext(loggerContext);
        fa.start();

        com.dalcomlab.ap.logback.classic.Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(fa);

        return loggerContext;
    }

    static void usage(String msg) {
        System.err.println(msg);
        System.err.println("Usage: java " + FileAppenderPerf.class.getName() + " filename");

        System.exit(1);
    }

    public static void main(String[] argv) throws Exception {
        if (argv.length > 1) {
            usage("Wrong number of arguments.");
        }

        if (argv.length == 0) {
            FILENAME = DIFF + "";
        } else {
            FILENAME = argv[0];
        }

        perfCase(false);
        perfCase(true);
    }

    static void perfCase(boolean safetyMode) throws Exception {
        LoggerContext lc = buildLoggerContext(FILENAME + "-" + safetyMode + ".log", safetyMode);
        Logger logger = lc.getLogger(FileAppenderPerf.class);

        long start = System.nanoTime();
        for (int i = 0; i < LEN; i++) {
            logger.debug(msgLong + " " + i);
        }
        // in microseconds
        double durationPerLog = (System.nanoTime() - start) / (LEN * 1000.0);

        lc.stop();

        System.out.println("Average duration of " + (durationPerLog) + " microseconds per log. Prudent mode=" + safetyMode);
        System.out.println("------------------------------------------------");
    }

}
