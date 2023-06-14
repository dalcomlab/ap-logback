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
package com.dalcomlab.logback.core.joran;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SkippingInInterpreterTest.class, TrivialConfiguratorTest.class, com.dalcomlab.logback.core.joran.action.PackageTest.class,
        com.dalcomlab.logback.core.joran.event.PackageTest.class, com.dalcomlab.logback.core.joran.util.PackageTest.class, com.dalcomlab.logback.core.joran.spi.PackageTest.class,
        com.dalcomlab.logback.core.joran.replay.PackageTest.class, com.dalcomlab.logback.core.joran.implicitAction.PackageTest.class,
        com.dalcomlab.logback.core.joran.conditional.PackageTest.class })
public class PackageTest {

}
