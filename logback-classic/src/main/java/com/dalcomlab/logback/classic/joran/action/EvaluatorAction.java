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

import com.dalcomlab.logback.classic.boolex.JaninoEventEvaluator;
import com.dalcomlab.logback.core.joran.action.AbstractEventEvaluatorAction;

public class EvaluatorAction extends AbstractEventEvaluatorAction {
    protected String defaultClassName() {
        return JaninoEventEvaluator.class.getName();
    }
}