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
package com.dalcomlab.ap.logback.classic.joran.action;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.jmx.JMXConfigurator;
import com.dalcomlab.ap.logback.classic.jmx.MBeanUtil;
import com.dalcomlab.ap.logback.core.joran.action.Action;
import com.dalcomlab.ap.logback.core.joran.spi.ActionException;
import com.dalcomlab.ap.logback.core.joran.spi.InterpretationContext;
import com.dalcomlab.ap.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class JMXConfiguratorAction extends Action {

    static final String OBJECT_NAME_ATTRIBUTE_NAME = "objectName";
    static final String CONTEXT_NAME_ATTRIBUTE_NAME = "contextName";
    static final char JMX_NAME_SEPARATOR = ',';

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
        addInfo("begin");

        String contextName = context.getName();
        String contextNameAttributeVal = attributes.getValue(CONTEXT_NAME_ATTRIBUTE_NAME);
        if (!OptionHelper.isEmpty(contextNameAttributeVal)) {
            contextName = contextNameAttributeVal;
        }

        String objectNameAsStr;
        String objectNameAttributeVal = attributes.getValue(OBJECT_NAME_ATTRIBUTE_NAME);
        if (OptionHelper.isEmpty(objectNameAttributeVal)) {
            objectNameAsStr = MBeanUtil.getObjectNameFor(contextName, JMXConfigurator.class);
        } else {
            objectNameAsStr = objectNameAttributeVal;
        }

        ObjectName objectName = MBeanUtil.string2ObjectName(context, this, objectNameAsStr);
        if (objectName == null) {
            addError("Failed construct ObjectName for [" + objectNameAsStr + "]");
            return;
        }

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        if (!MBeanUtil.isRegistered(mbs, objectName)) {
            // register only of the named JMXConfigurator has not been previously
            // registered. Unregistering an MBean within invocation of itself
            // caused jconsole to throw an NPE. (This occurs when the reload* method
            // unregisters the
            JMXConfigurator jmxConfigurator = new JMXConfigurator((LoggerContext) context, mbs, objectName);
            try {
                mbs.registerMBean(jmxConfigurator, objectName);
            } catch (Exception e) {
                addError("Failed to create mbean", e);
            }
        }

    }

    @Override
    public void end(InterpretationContext ec, String name) throws ActionException {

    }

}
