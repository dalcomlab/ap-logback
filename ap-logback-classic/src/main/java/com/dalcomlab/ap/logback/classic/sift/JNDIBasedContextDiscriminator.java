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
package com.dalcomlab.ap.logback.classic.sift;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.selector.ContextSelector;
import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.classic.util.ContextSelectorStaticBinder;
import com.dalcomlab.ap.logback.core.sift.AbstractDiscriminator;

/**
 * This discriminator returns the value context as determined by JNDI. If the
 * said value is null, then a default value is returned.
 * 
 * <p>
 * Both Key and the DefaultValue are user specified properties.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class JNDIBasedContextDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

    private static final String KEY = "contextName";
    private String defaultValue;

    /**
     * Return the name of the current context name as found in the logging event.
     */
    public String getDiscriminatingValue(ILoggingEvent event) {
        ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();

        if (selector == null) {
            return defaultValue;
        }

        LoggerContext lc = selector.getLoggerContext();
        if (lc == null) {
            return defaultValue;
        }

        return lc.getName();
    }

    public String getKey() {
        return KEY;
    }

    public void setKey(String key) {
        throw new UnsupportedOperationException("Key cannot be set. Using fixed key " + KEY);
    }

    /**
     * @see #setDefaultValue(String)
     * @return
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * The default context name in case the context name is not set for the
     * current logging event.
     * 
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
