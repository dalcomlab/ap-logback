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
package com.dalcomlab.logback.classic.joran.action;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.LoggerContext;
import com.dalcomlab.logback.core.joran.action.Action;
import com.dalcomlab.logback.core.joran.action.ActionConst;
import com.dalcomlab.logback.core.joran.spi.InterpretationContext;
import com.dalcomlab.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

public class RootLoggerAction extends Action {

    Logger root;
    boolean inError = false;

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        inError = false;

        LoggerContext loggerContext = (LoggerContext) this.context;
        root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

        String levelStr = ec.subst(attributes.getValue(ActionConst.LEVEL_ATTRIBUTE));
        if (!OptionHelper.isEmpty(levelStr)) {
            Level level = Level.toLevel(levelStr);
            addInfo("Setting level of ROOT logger to " + level);
            root.setLevel(level);
        }
        ec.pushObject(root);
    }

    public void end(InterpretationContext ec, String name) {
        if (inError) {
            return;
        }
        Object o = ec.peekObject();
        if (o != root) {
            addWarn("The object on the top the of the stack is not the root logger");
            addWarn("It is: " + o);
        } else {
            ec.popObject();
        }
    }

    public void finish(InterpretationContext ec) {
    }
}
