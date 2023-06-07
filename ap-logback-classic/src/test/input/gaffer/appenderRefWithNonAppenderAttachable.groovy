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
import com.dalcomlab.ap.logback.classic.PatternLayout
import com.dalcomlab.ap.logback.core.ConsoleAppender
import com.dalcomlab.ap.logback.core.encoder.LayoutWrappingEncoder

appender("STDOUT", ConsoleAppender) {
  encoder(LayoutWrappingEncoder) {
    layout(PatternLayout) {
      pattern = "%m%n"
    }
  }
}

appender("STDOUT-WITH-APPENDERREF", ConsoleAppender) {
  appenderRef('STDOUT')
}

root(DEBUG, ["STDOUT-ASYNC"])
