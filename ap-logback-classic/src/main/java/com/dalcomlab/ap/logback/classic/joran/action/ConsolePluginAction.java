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
package com.dalcomlab.ap.logback.classic.joran.action;

import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.net.SocketAppender;
import com.dalcomlab.ap.logback.core.joran.action.Action;
import com.dalcomlab.ap.logback.core.joran.spi.ActionException;
import com.dalcomlab.ap.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

public class ConsolePluginAction extends Action {

    private static final String PORT_ATTR = "port";
    private static final Integer DEFAULT_PORT = 4321;

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
        String portStr = attributes.getValue(PORT_ATTR);
        Integer port = null;

        if (portStr == null) {
            port = DEFAULT_PORT;
        } else {
            try {
                port = Integer.valueOf(portStr);
            } catch (NumberFormatException ex) {
                addError("Port " + portStr + " in ConsolePlugin config is not a correct number");
            }
        }

        LoggerContext lc = (LoggerContext) ec.getContext();
        SocketAppender appender = new SocketAppender();
        appender.setContext(lc);
        appender.setIncludeCallerData(true);
        appender.setRemoteHost("localhost");
        appender.setPort(port.intValue());
        appender.start();
        Logger root = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(appender);

        addInfo("Sending LoggingEvents to the plugin using port " + port);
    }

    @Override
    public void end(InterpretationContext ec, String name) throws ActionException {

    }
}
