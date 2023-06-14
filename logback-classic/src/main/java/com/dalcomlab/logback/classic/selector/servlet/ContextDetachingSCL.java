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
package com.dalcomlab.logback.classic.selector.servlet;

import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.selector.ContextSelector;
import com.dalcomlab.logback.classic.util.ContextSelectorStaticBinder;
import com.dalcomlab.logback.core.util.JNDIUtil;
import com.dalcomlab.logback.core.util.OptionHelper;
import org.slf4j.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static com.dalcomlab.logback.classic.ClassicConstants.JNDI_CONTEXT_NAME;

public class ContextDetachingSCL implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        // do nothing
    }
    
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        String loggerContextName = null;

        try {
            Context ctx = JNDIUtil.getInitialContext();
            loggerContextName = (String) JNDIUtil.lookupString(ctx, JNDI_CONTEXT_NAME);
        } catch (NamingException ne) {
        }

        if (!OptionHelper.isEmpty(loggerContextName)) {
            System.out.println("About to detach context named " + loggerContextName);

            ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
            if (selector == null) {
                System.out.println("Selector is null, cannot detach context. Skipping.");
                return;
            }
            LoggerContext context = selector.getLoggerContext(loggerContextName);
            if (context != null) {
                Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
                logger.warn("Stopping logger context " + loggerContextName);
                selector.detachLoggerContext(loggerContextName);
                // when the web-app is destroyed, its logger context should be stopped
                context.stop();
            } else {
                System.out.println("No context named " + loggerContextName + " was found.");
            }
        }
    }

 

}
