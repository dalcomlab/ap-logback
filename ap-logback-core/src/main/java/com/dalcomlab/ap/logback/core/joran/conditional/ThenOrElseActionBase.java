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
package com.dalcomlab.ap.logback.core.joran.conditional;

import com.dalcomlab.ap.logback.core.joran.action.Action;
import com.dalcomlab.ap.logback.core.joran.event.InPlayListener;
import com.dalcomlab.ap.logback.core.joran.event.SaxEvent;
import com.dalcomlab.ap.logback.core.joran.spi.ActionException;
import com.dalcomlab.ap.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

abstract public class ThenOrElseActionBase extends Action {

    Stack<ThenActionState> stateStack = new Stack<ThenActionState>();

    @Override
    public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {

        if (!weAreActive(ic))
            return;

        ThenActionState state = new ThenActionState();
        if (ic.isListenerListEmpty()) {
            ic.addInPlayListener(state);
            state.isRegistered = true;
        }
        stateStack.push(state);
    }

    boolean weAreActive(InterpretationContext ic) {
        Object o = ic.peekObject();
        if (!(o instanceof IfAction))
            return false;
        IfAction ifAction = (IfAction) o;
        return ifAction.isActive();
    }

    @Override
    public void end(InterpretationContext ic, String name) throws ActionException {
        if (!weAreActive(ic))
            return;

        ThenActionState state = stateStack.pop();
        if (state.isRegistered) {
            ic.removeInPlayListener(state);
            Object o = ic.peekObject();
            if (o instanceof IfAction) {
                IfAction ifAction = (IfAction) o;
                removeFirstAndLastFromList(state.eventList);
                registerEventList(ifAction, state.eventList);
            } else {
                throw new IllegalStateException("Missing IfAction on top of stack");
            }
        }
    }

    abstract void registerEventList(IfAction ifAction, List<SaxEvent> eventList);

    void removeFirstAndLastFromList(List<SaxEvent> eventList) {
        eventList.remove(0);
        eventList.remove(eventList.size() - 1);
    }

}

class ThenActionState implements InPlayListener {

    List<SaxEvent> eventList = new ArrayList<SaxEvent>();
    boolean isRegistered = false;

    public void inPlay(SaxEvent event) {
        eventList.add(event);
    }
}
