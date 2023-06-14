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
package com.dalcomlab.logback.core.joran.replay;

import com.dalcomlab.logback.core.joran.GenericConfigurator;
import com.dalcomlab.logback.core.joran.action.NOPAction;
import com.dalcomlab.logback.core.joran.action.NestedBasicPropertyIA;
import com.dalcomlab.logback.core.joran.action.NestedComplexPropertyIA;
import com.dalcomlab.logback.core.joran.event.SaxEvent;
import com.dalcomlab.logback.core.joran.spi.*;

import java.util.List;

public class FruitConfigurator extends GenericConfigurator {

    FruitFactory ff;

    public FruitConfigurator(FruitFactory ff) {
        this.ff = ff;
    }

    @Override
    final public void doConfigure(final List<SaxEvent> eventList) throws JoranException {
        buildInterpreter();
        interpreter.getInterpretationContext().pushObject(ff);
        EventPlayer player = new EventPlayer(interpreter);
        player.play(eventList);
    }

    @Override
    protected void addImplicitRules(Interpreter interpreter) {
        NestedComplexPropertyIA nestedIA = new NestedComplexPropertyIA(getBeanDescriptionCache());
        nestedIA.setContext(context);
        interpreter.addImplicitAction(nestedIA);

        NestedBasicPropertyIA nestedSimpleIA = new NestedBasicPropertyIA(getBeanDescriptionCache());
        nestedIA.setContext(context);
        interpreter.addImplicitAction(nestedSimpleIA);
    }

    @Override
    protected void addInstanceRules(RuleStore rs) {
        rs.addRule(new ElementSelector("fruitShell"), new NOPAction());
    }

}
