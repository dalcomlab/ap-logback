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
package com.dalcomlab.ap.logback.classic.pattern;

import com.dalcomlab.ap.logback.classic.spi.ILoggingEvent;
import com.dalcomlab.ap.logback.core.CoreConstants;
import com.dalcomlab.ap.logback.core.util.CachingDateFormatter;

import java.util.List;
import java.util.TimeZone;

public class DateConverter extends ClassicConverter {

    long lastTimestamp = -1;
    String timestampStrCache = null;
    CachingDateFormatter cachingDateFormatter = null;

    public void start() {

        String datePattern = getFirstOption();
        if (datePattern == null) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        if (datePattern.equals(CoreConstants.ISO8601_STR)) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        try {
            cachingDateFormatter = new CachingDateFormatter(datePattern);
            // maximumCacheValidity =
            // CachedDateFormat.getMaximumCacheValidity(pattern);
        } catch (IllegalArgumentException e) {
            addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);
            // default to the ISO8601 format
            cachingDateFormatter = new CachingDateFormatter(CoreConstants.ISO8601_PATTERN);
        }

        List<String> optionList = getOptionList();

        // if the option list contains a TZ option, then set it.
        if (optionList != null && optionList.size() > 1) {
            TimeZone tz = TimeZone.getTimeZone((String) optionList.get(1));
            cachingDateFormatter.setTimeZone(tz);
        }
    }

    public String convert(ILoggingEvent le) {
        long timestamp = le.getTimeStamp();
        return cachingDateFormatter.format(timestamp);
    }
}
