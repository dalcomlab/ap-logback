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
//
// Built on Wed May 19 20:51:44 CEST 2010 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html
//

import com.dalcomlab.logback.classic.boolex.JaninoEventEvaluator
import com.dalcomlab.logback.classic.encoder.PatternLayoutEncoder
import com.dalcomlab.logback.core.ConsoleAppender
import com.dalcomlab.logback.core.filter.EvaluatorFilter

import static com.dalcomlab.logback.classic.Level.DEBUG
import static com.dalcomlab.logback.core.spi.FilterReply.DENY
import static com.dalcomlab.logback.core.spi.FilterReply.NEUTRAL
import com.dalcomlab.logback.core.boolex.Matcher
import com.dalcomlab.logback.core.spi.LifeCycle

appender("STDOUT", ConsoleAppender) {
  filter(EvaluatorFilter) {
    evaluator(JaninoEventEvaluator) {
      Matcher aMatcher = new Matcher()
      aMatcher.name = "odd"
      aMatcher.regex = "statement [13579]"
      if(aMatcher instanceof LifeCycle)
        aMatcher.start();
      aMatcher.start();
      matcher = aMatcher
      expression = "odd.matches(formattedMessage)"
    }
    OnMismatch = NEUTRAL
    OnMatch = DENY
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%-4relative [%thread] %-5level %logger - %msg%n"
  }
}
root(DEBUG, ["STDOUT"])
