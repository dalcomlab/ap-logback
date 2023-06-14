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
package com.dalcomlab.logback.core.joran.action;

import com.dalcomlab.logback.core.joran.util.PropertySetter;
import com.dalcomlab.logback.core.util.AggregationType;

/**
 * Lump together several fields for use by {@link NestedBasicPropertyIA}.
 * 
 * @author Ceki Gulcu
 */
class IADataForBasicProperty {
    final PropertySetter parentBean;
    final AggregationType aggregationType;
    final String propertyName;
    boolean inError;

    IADataForBasicProperty(PropertySetter parentBean, AggregationType aggregationType, String propertyName) {
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.propertyName = propertyName;
    }
}