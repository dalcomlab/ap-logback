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
package com.dalcomlab.logback.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicStatusManagerTest.class, com.dalcomlab.logback.core.status.PackageTest.class, com.dalcomlab.logback.core.util.PackageTest.class,
        com.dalcomlab.logback.core.helpers.PackageTest.class, com.dalcomlab.logback.core.subst.PackageTest.class, com.dalcomlab.logback.core.pattern.PackageTest.class,
        PackageTest.class, com.dalcomlab.logback.core.joran.PackageTest.class, com.dalcomlab.logback.core.appender.PackageTest.class,
        com.dalcomlab.logback.core.spi.PackageTest.class, com.dalcomlab.logback.core.rolling.PackageTest.class, com.dalcomlab.logback.core.net.PackageTest.class,
        com.dalcomlab.logback.core.sift.PackageTest.class, com.dalcomlab.logback.core.encoder.PackageTest.class, com.dalcomlab.logback.core.recovery.PackageTest.class })
public class AllCoreTest {
}
