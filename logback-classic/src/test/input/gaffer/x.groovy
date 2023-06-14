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
import com.dalcomlab.logback.classic.PatternLayout
import com.dalcomlab.logback.classic.filter.ThresholdFilter
import com.dalcomlab.logback.classic.net.SMTPAppender
import com.dalcomlab.logback.core.ConsoleAppender
import com.dalcomlab.logback.core.rolling.FixedWindowRollingPolicy
import com.dalcomlab.logback.core.rolling.RollingFileAppender
import com.dalcomlab.logback.core.rolling.SizeBasedTriggeringPolicy


import static com.dalcomlab.logback.classic.Level.ERROR
import static com.dalcomlab.logback.classic.Level.INFO



appender("RootFileAppender", RollingFileAppender) {
  file = "project"
  append = true
  filter(ThresholdFilter) {
    level = INFO
  }
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "project_log.%i"
    maxIndex = 1
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = 1000000
  }

  layout(PatternLayout) {
    pattern = "%d{yyyy-MM-dd HH:mm:ss}, %p, %c, %t, %ex, %F, %L, %C{1}, %M %m%n"
  }
}

appender("RootConsoleAppender", ConsoleAppender) {
  filter(ThresholdFilter) {
    level = INFO
  }

  layout(PatternLayout) {
    pattern = "%d{yyyy-MM-dd HH:mm:ss}, %p, %c, %t %m%n"
  }
}

appender("RootEmailAppender", SMTPAppender) {
  filter(ThresholdFilter) {
    level = ERROR
  }
  bufferSize = 10
  SMTPHost = "smtp.hostaddress"
  to = "logback.user@xyz.com"
  from = " logback.user@xyz.com "
  username = "user"
  password = "password"
  subject = "Logback Error"

  layout(PatternLayout) {
    pattern = "%d{yyyy-MM-dd HH:mm:ss}, %p, %c, %t, %ex, %F, %L, %C{1}, %M %m%n"
  }

}

root(INFO, ["RootFileAppender", "RootConsoleAppender", "RootEmailAppender"])