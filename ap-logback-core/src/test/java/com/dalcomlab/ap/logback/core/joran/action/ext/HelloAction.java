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
package com.dalcomlab.ap.logback.core.joran.action.ext;

import com.dalcomlab.ap.logback.core.joran.action.Action;
import com.dalcomlab.ap.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

public class HelloAction extends Action {

    static final public String PROPERTY_KEY = "name";

    /**
     * Instantiates an layout of the given class and sets its name.
     *
     */
    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        String str = "Hello " + attributes.getValue("name") + ".";
        ec.getContext().putProperty(PROPERTY_KEY, str);
    }

    /**
     * Once the children elements are also parsed, now is the time to activate
     * the appender options.
     */
    public void end(InterpretationContext ec, String name) {
    }
}
