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
package com.dalcomlab.ap.logback.classic.issue.lbclassic180;

import com.dalcomlab.ap.logback.classic.pattern.ClassicConverter;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.helpers.Transform;

public class HtmlEscapedMessageConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return Transform.escapeTags(event.getFormattedMessage());
    }
}
