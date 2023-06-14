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
package com.dalcomlab.logback.core.net.mock;

import com.dalcomlab.logback.core.Context;
import com.dalcomlab.logback.core.ContextBase;
import com.dalcomlab.logback.core.status.Status;
import com.dalcomlab.logback.core.status.StatusListener;
import com.dalcomlab.logback.core.status.StatusManager;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A mock {@link Context} with instrumentation for unit testing.
 *
 * @author Carl Harris
 */
public class MockContext extends ContextBase {

    private final ScheduledExecutorService scheduledExecutorService;

    private Status lastStatus;

    public MockContext() {
        this(new MockScheduledExecutorService());
    }

    public MockContext(ScheduledExecutorService executorService) {
        this.setStatusManager(new MockStatusManager());
        this.scheduledExecutorService = executorService;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    private class MockStatusManager implements StatusManager {

        public void add(Status status) {
            lastStatus = status;
        }

        public List<Status> getCopyOfStatusList() {
            throw new UnsupportedOperationException();
        }

        public int getCount() {
            throw new UnsupportedOperationException();
        }

        public boolean add(StatusListener listener) {
            throw new UnsupportedOperationException();
        }

        public void remove(StatusListener listener) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public List<StatusListener> getCopyOfStatusListenerList() {
            throw new UnsupportedOperationException();
        }

    }

}
