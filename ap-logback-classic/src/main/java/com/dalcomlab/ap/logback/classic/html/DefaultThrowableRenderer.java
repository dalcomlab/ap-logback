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
package com.dalcomlab.ap.logback.classic.html;

import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.IThrowableProxy;
import com.dalcomlab.ap.logback.classic.spi.StackTraceElementProxy;
import com.dalcomlab.ap.logback.core.CoreConstants;
import com.dalcomlab.ap.logback.core.helpers.Transform;
import com.dalcomlab.ap.logback.core.html.IThrowableRenderer;

public class DefaultThrowableRenderer implements IThrowableRenderer<ILoggingEvent> {

    static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";

    public void render(StringBuilder sbuf, ILoggingEvent event) {
        IThrowableProxy tp = event.getThrowableProxy();
        sbuf.append("<tr><td class=\"Exception\" colspan=\"6\">");
        while (tp != null) {
            render(sbuf, tp);
            tp = tp.getCause();
        }
        sbuf.append("</td></tr>");
    }

    void render(StringBuilder sbuf, IThrowableProxy tp) {
        printFirstLine(sbuf, tp);

        int commonFrames = tp.getCommonFrames();
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();

        for (int i = 0; i < stepArray.length - commonFrames; i++) {
            StackTraceElementProxy step = stepArray[i];
            sbuf.append(TRACE_PREFIX);
            sbuf.append(Transform.escapeTags(step.toString()));
            sbuf.append(CoreConstants.LINE_SEPARATOR);
        }

        if (commonFrames > 0) {
            sbuf.append(TRACE_PREFIX);
            sbuf.append("\t... ").append(commonFrames).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
        }
    }

    public void printFirstLine(StringBuilder sb, IThrowableProxy tp) {
        int commonFrames = tp.getCommonFrames();
        if (commonFrames > 0) {
            sb.append("<br />").append(CoreConstants.CAUSED_BY);
        }
        sb.append(tp.getClassName()).append(": ").append(Transform.escapeTags(tp.getMessage()));
        sb.append(CoreConstants.LINE_SEPARATOR);
    }

}
