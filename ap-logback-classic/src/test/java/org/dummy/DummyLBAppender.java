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
package org.dummy;

import com.dalcomlab.ap.logback.classic.PatternLayout;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class DummyLBAppender extends AppenderBase<ILoggingEvent> {

    public List<ILoggingEvent> list = new ArrayList<ILoggingEvent>();
    public List<String> stringList = new ArrayList<String>();

    PatternLayout layout;

    DummyLBAppender() {
        this(null);
    }

    DummyLBAppender(PatternLayout layout) {
        this.layout = layout;
    }

    protected void append(ILoggingEvent e) {
        list.add(e);
        if (layout != null) {
            String s = layout.doLayout(e);
            stringList.add(s);
        }
    }
}
