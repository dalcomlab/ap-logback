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
package com.dalcomlab.ap.logback.core.rolling.helper;

import com.dalcomlab.ap.logback.core.util.DatePatternToRegexTest;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CompressTest.class, FileNamePatternTest.class, RollingCalendarTest.class, DatePatternToRegexTest.class })
public class PackageTest extends TestCase {

}
