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
package com.dalcomlab.ap.logback.classic.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestHelper {

    private static final Method ADD_SUPPRESSED_METHOD;

    static {
        Method method = null;
        try {
            method = Throwable.class.getMethod("addSuppressed", Throwable.class);
        } catch (NoSuchMethodException e) {
            // ignore, will get thrown in Java < 7
        }
        ADD_SUPPRESSED_METHOD = method;
    }

    public static boolean suppressedSupported() {
        return ADD_SUPPRESSED_METHOD != null;
    }

    public static void addSuppressed(Throwable outer, Throwable suppressed) throws InvocationTargetException, IllegalAccessException {
        if (suppressedSupported()) {
            ADD_SUPPRESSED_METHOD.invoke(outer, suppressed);
        }
    }

    static public Throwable makeNestedException(int level) {
        if (level == 0) {
            return new Exception("nesting level=" + level);
        }
        Throwable cause = makeNestedException(level - 1);
        return new Exception("nesting level =" + level, cause);
    }

    /**
     * Usage:
     * <pre>
     * String s = "123";
     * positionOf("1").in(s) < positionOf("3").in(s)
     * </pre>
     *
     * @param pattern Plain text to be found
     * @return StringPosition fluent interface
     */
    public static StringPosition positionOf(String pattern) {
        return new StringPosition(pattern);
    }

    public static class StringPosition {
        private final String pattern;

        public StringPosition(String pattern) {
            this.pattern = pattern;
        }

        public int in(String s) {
            final int position = s.indexOf(pattern);
            if (position < 0)
                throw new IllegalArgumentException("String '" + pattern + "' not found in: '" + s + "'");
            return position;
        }

    }

}
