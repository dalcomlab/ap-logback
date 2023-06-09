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
package com.dalcomlab.ap.logback.core.net.server;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A mock {@link BlockingQueue} with instrumentation for unit testing.
 *
 * @author Carl Harris
 */
class MockEventQueue extends LinkedBlockingQueue<Serializable> {

    private static final long serialVersionUID = 1L;

    @Override
    public Serializable take() throws InterruptedException {
        if (isEmpty()) {
            Thread.currentThread().interrupt();
        }
        return super.take();
    }

}
