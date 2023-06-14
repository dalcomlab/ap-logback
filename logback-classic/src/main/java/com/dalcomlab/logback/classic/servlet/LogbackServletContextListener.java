package com.dalcomlab.logback.classic.servlet;

import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.classic.util.StatusViaSLF4JLoggerFactory;
import com.dalcomlab.logback.core.spi.ContextAwareBase;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Allows for graceful shutdown of the {@link LoggerContext} associated with this web-app.
 * 
 * @author Ceki Gulcu
 * @since 1.1.10
 */
public class LogbackServletContextListener implements ServletContextListener {

    ContextAwareBase contextAwareBase = new ContextAwareBase();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
            contextAwareBase.setContext(loggerContext);
            StatusViaSLF4JLoggerFactory.addInfo("About to stop " + loggerContext.getClass().getCanonicalName() + " [" + loggerContext.getName() + "]", this);
            loggerContext.stop();
        }
    }

}
