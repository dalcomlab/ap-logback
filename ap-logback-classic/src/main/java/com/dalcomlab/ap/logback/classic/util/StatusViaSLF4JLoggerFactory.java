package com.dalcomlab.ap.logback.classic.util;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.core.spi.ContextAwareBase;
import com.dalcomlab.ap.logback.core.status.ErrorStatus;
import com.dalcomlab.ap.logback.core.status.InfoStatus;
import com.dalcomlab.ap.logback.core.status.Status;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

/**
 * Add a status message to the {@link LoggerContext} returned by {@link LoggerFactory#getILoggerFactory}.
 * @author ceki
 * @since 1.1.10
 */
public class StatusViaSLF4JLoggerFactory {

    public static void addInfo(String msg, Object o) {
        addStatus(new InfoStatus(msg, o));
    }

    public static void addError(String msg, Object o) {
        addStatus(new ErrorStatus(msg, o));
    }

    public static void addError(String msg, Object o, Throwable t) {
        addStatus(new ErrorStatus(msg, o, t));
    }

    public static void addStatus(Status status) {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext) {
            ContextAwareBase contextAwareBase = new ContextAwareBase();
            LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
            contextAwareBase.setContext(loggerContext);
            contextAwareBase.addStatus(status);
        }
    }
}
