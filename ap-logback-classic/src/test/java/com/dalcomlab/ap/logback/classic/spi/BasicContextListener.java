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
package com.dalcomlab.ap.logback.classic.spi;

import com.dalcomlab.ap.logback.classic.Level;
import com.dalcomlab.ap.logback.classic.Logger;
import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.spi.LoggerContextListener;

public class BasicContextListener implements LoggerContextListener {

    enum UpdateType {
        NONE, START, RESET, STOP, LEVEL_CHANGE
    };

    UpdateType updateType = UpdateType.NONE;
    LoggerContext context;
    Logger logger;
    Level level;

    boolean resetResistant;

    public void setResetResistant(boolean resetResistant) {
        this.resetResistant = resetResistant;
    }

    public void onReset(LoggerContext context) {
        updateType = UpdateType.RESET;
        this.context = context;

    }

    public void onStart(LoggerContext context) {
        updateType = UpdateType.START;
        ;
        this.context = context;
    }

    public void onStop(LoggerContext context) {
        updateType = UpdateType.STOP;
        ;
        this.context = context;
    }

    public boolean isResetResistant() {
        return resetResistant;
    }

    public void onLevelChange(Logger logger, Level level) {
        updateType = UpdateType.LEVEL_CHANGE;
        this.logger = logger;
        this.level = level;
    }

}
