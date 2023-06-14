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
package com.dalcomlab.logback.classic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ org.slf4j.impl.PackageTest.class, PackageTest.class, com.dalcomlab.logback.classic.util.PackageTest.class,
        com.dalcomlab.logback.classic.control.PackageTest.class, com.dalcomlab.logback.classic.joran.PackageTest.class, com.dalcomlab.logback.classic.rolling.PackageTest.class,
        com.dalcomlab.logback.classic.jmx.PackageTest.class, com.dalcomlab.logback.classic.boolex.PackageTest.class, com.dalcomlab.logback.classic.selector.PackageTest.class,
        com.dalcomlab.logback.classic.html.PackageTest.class, com.dalcomlab.logback.classic.net.PackageTest.class, com.dalcomlab.logback.classic.pattern.PackageTest.class,
        com.dalcomlab.logback.classic.encoder.PackageTest.class, com.dalcomlab.logback.classic.spi.PackageTest.class,
        com.dalcomlab.logback.classic.turbo.PackageTest.class, com.dalcomlab.logback.classic.sift.PackageTest.class, com.dalcomlab.logback.classic.jul.PackageTest.class,
        com.dalcomlab.logback.classic.issue.PackageTest.class })
public class AllClassicTest {

}
