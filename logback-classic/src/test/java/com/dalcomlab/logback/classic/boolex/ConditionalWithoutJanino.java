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

import com.dalcomlab.logback.classic.ClassicTestConstants;
import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.joran.JoranConfigurator;
import com.dalcomlab.logback.core.joran.conditional.IfAction;
import com.dalcomlab.logback.core.joran.spi.JoranException;
import com.dalcomlab.logback.core.status.StatusChecker;
import com.dalcomlab.logback.core.util.StatusPrinter;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * @author Ceki G&uuml;lc&uuml;
 */
public class ConditionalWithoutJanino {

    LoggerContext loggerContext = new LoggerContext();
    Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

    void configure(String file) throws JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        jc.doConfigure(file);
    }

    // assume that janino.jar ia NOT on the classpath
    @Test
    public void conditionalWithoutJanino() throws JoranException {
        String configFile = ClassicTestConstants.JORAN_INPUT_PREFIX + "conditional/withoutJanino.xml";
        String currentDir = System.getProperty("user.dir");
        if (!currentDir.contains("logback-classic")) {
            configFile = "logback-classic/" + configFile;
        }
        configure(configFile);
        StatusPrinter.print(loggerContext);
        StatusChecker checker = new StatusChecker(loggerContext);
        checker.assertContainsMatch(IfAction.MISSING_JANINO_MSG);

        assertSame(Level.WARN, loggerContext.getLogger("a").getLevel());
        assertSame(Level.WARN, root.getLevel());
    }

}
