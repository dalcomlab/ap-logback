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
package com.dalcomlab.logback.classic.issue.lbcore211;

import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.joran.JoranConfigurator;
import com.dalcomlab.logback.core.joran.spi.JoranException;
import com.dalcomlab.logback.core.util.StatusPrinter;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * @author Ceki G&uuml;lc&uuml;
 */
public class Lbcore211 {

    @Test
    public void lbcore211() throws JoranException {

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        configurator.doConfigure("/home/ceki/lbcore211.xml");

        Logger l = lc.getLogger("file.logger");
        StatusPrinter.print(lc);
        for (int i = 0; i < 10; i++) {
            l.info("hello " + i);
        }

        lc.stop();
    }
}
