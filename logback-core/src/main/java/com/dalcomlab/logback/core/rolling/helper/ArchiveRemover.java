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
package com.dalcomlab.logback.core.rolling.helper;

import com.dalcomlab.logback.core.spi.ContextAware;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * Given a date remove older archived log files.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public interface ArchiveRemover extends ContextAware {
    void clean(Date now);
    void setMaxHistory(int maxHistory);
    void setTotalSizeCap(long totalSizeCap);
    Future<?> cleanAsynchronously(Date now);
}