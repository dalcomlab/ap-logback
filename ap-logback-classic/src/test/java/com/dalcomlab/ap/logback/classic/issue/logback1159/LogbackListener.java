package com.dalcomlab.ap.logback.classic.issue.logback1159;

import com.dalcomlab.ap.logback.core.spi.ContextAwareBase;
import com.dalcomlab.ap.logback.core.spi.LifeCycle;
import com.dalcomlab.ap.logback.core.status.ErrorStatus;
import com.dalcomlab.ap.logback.core.status.Status;
import com.dalcomlab.ap.logback.core.status.StatusListener;

import java.io.IOException;

public class LogbackListener extends ContextAwareBase implements StatusListener, LifeCycle {
    private boolean started;

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void addStatusEvent(final Status status) {
        if (status instanceof ErrorStatus && status.getThrowable() instanceof IOException) {
            System.out.println("*************************LogbackListener.addStatusEvent");
            throw new LoggingError(status.getMessage(), status.getThrowable());
        }
    }

}