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
package com.dalcomlab.ap.logback.classic.joran;

import com.dalcomlab.ap.logback.classic.joran.action.*;
import com.dalcomlab.ap.logback.classic.sift.SiftAction;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.spi.PlatformInfo;
import com.dalcomlab.ap.logback.classic.util.DefaultNestedComponentRules;
import com.dalcomlab.ap.logback.core.joran.JoranConfiguratorBase;
import com.dalcomlab.ap.logback.core.joran.action.AppenderRefAction;
import com.dalcomlab.ap.logback.core.joran.action.IncludeAction;
import com.dalcomlab.ap.logback.core.joran.action.NOPAction;
import com.dalcomlab.ap.logback.core.joran.conditional.ElseAction;
import com.dalcomlab.ap.logback.core.joran.conditional.IfAction;
import com.dalcomlab.ap.logback.core.joran.conditional.ThenAction;
import com.dalcomlab.ap.logback.core.joran.spi.DefaultNestedComponentRegistry;
import com.dalcomlab.ap.logback.core.joran.spi.ElementSelector;
import com.dalcomlab.ap.logback.core.joran.spi.RuleStore;

/**
 * JoranConfigurator class adds rules specific to logback-classic.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class JoranConfigurator extends JoranConfiguratorBase<ILoggingEvent> {

    @Override
    public void addInstanceRules(RuleStore rs) {
        // parent rules already added
        super.addInstanceRules(rs);

        rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());

        rs.addRule(new ElementSelector("configuration/contextName"), new ContextNameAction());
        rs.addRule(new ElementSelector("configuration/contextListener"), new LoggerContextListenerAction());
        rs.addRule(new ElementSelector("configuration/insertFromJNDI"), new InsertFromJNDIAction());
        rs.addRule(new ElementSelector("configuration/evaluator"), new EvaluatorAction());

        rs.addRule(new ElementSelector("configuration/appender/sift"), new SiftAction());
        rs.addRule(new ElementSelector("configuration/appender/sift/*"), new NOPAction());

        rs.addRule(new ElementSelector("configuration/logger"), new LoggerAction());
        rs.addRule(new ElementSelector("configuration/logger/level"), new LevelAction());

        rs.addRule(new ElementSelector("configuration/root"), new RootLoggerAction());
        rs.addRule(new ElementSelector("configuration/root/level"), new LevelAction());
        rs.addRule(new ElementSelector("configuration/logger/appender-ref"), new AppenderRefAction<ILoggingEvent>());
        rs.addRule(new ElementSelector("configuration/root/appender-ref"), new AppenderRefAction<ILoggingEvent>());

        // add if-then-else support
        rs.addRule(new ElementSelector("*/if"), new IfAction());
        rs.addRule(new ElementSelector("*/if/then"), new ThenAction());
        rs.addRule(new ElementSelector("*/if/then/*"), new NOPAction());
        rs.addRule(new ElementSelector("*/if/else"), new ElseAction());
        rs.addRule(new ElementSelector("*/if/else/*"), new NOPAction());

        // add jmxConfigurator only if we have JMX available.
        // If running under JDK 1.4 (retrotranslateed logback) then we
        // might not have JMX.
        if (PlatformInfo.hasJMXObjectName()) {
            rs.addRule(new ElementSelector("configuration/jmxConfigurator"), new JMXConfiguratorAction());
        }
        rs.addRule(new ElementSelector("configuration/include"), new IncludeAction());

        rs.addRule(new ElementSelector("configuration/consolePlugin"), new ConsolePluginAction());

        rs.addRule(new ElementSelector("configuration/receiver"), new ReceiverAction());

    }

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
    }

}
