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
package com.dalcomlab.ap.logback.core.util;

import com.dalcomlab.ap.logback.core.Context;
import com.dalcomlab.ap.logback.core.ContextBase;
import com.dalcomlab.ap.logback.core.status.ErrorStatus;
import com.dalcomlab.ap.logback.core.status.InfoStatus;
import com.dalcomlab.ap.logback.core.status.Status;
import com.dalcomlab.ap.logback.core.status.WarnStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class StatusPrinterTest {

    ByteArrayOutputStream outputStream;
    PrintStream ps;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        ps = new PrintStream(outputStream);
        StatusPrinter.setPrintStream(ps);
    }

    @After
    public void tearDown() throws Exception {
        StatusPrinter.setPrintStream(System.out);
        ps = null;
        outputStream = null;
    }

    @Test
    public void testBasic() {
        Context context = new ContextBase();
        context.getStatusManager().add(new InfoStatus("test", this));
        StatusPrinter.print(context);
        String result = outputStream.toString();
        assertTrue(result.contains("|-INFO in " + this.getClass().getName()));
    }

    @Test
    public void testNested() {
        Status s0 = new ErrorStatus("test0", this);
        Status s1 = new InfoStatus("test1", this);
        Status s11 = new InfoStatus("test11", this);
        Status s12 = new InfoStatus("test12", this);
        s1.add(s11);
        s1.add(s12);

        Status s2 = new InfoStatus("test2", this);
        Status s21 = new InfoStatus("test21", this);
        Status s211 = new WarnStatus("test211", this);

        Status s22 = new InfoStatus("test22", this);
        s2.add(s21);
        s2.add(s22);
        s21.add(s211);

        Context context = new ContextBase();
        context.getStatusManager().add(s0);
        context.getStatusManager().add(s1);
        context.getStatusManager().add(s2);

        StatusPrinter.print(context);
        String result = outputStream.toString();
        assertTrue(result.contains("+ INFO in " + this.getClass().getName()));
        assertTrue(result.contains("+ WARN in " + this.getClass().getName()));
        assertTrue(result.contains("    |-WARN in " + this.getClass().getName()));
    }

    @Test
    public void testWithException() {
        Status s0 = new ErrorStatus("test0", this);
        Status s1 = new InfoStatus("test1", this, new Exception("testEx"));
        Status s11 = new InfoStatus("test11", this);
        Status s12 = new InfoStatus("test12", this);
        s1.add(s11);
        s1.add(s12);

        Status s2 = new InfoStatus("test2", this);
        Status s21 = new InfoStatus("test21", this);
        Status s211 = new WarnStatus("test211", this);

        Status s22 = new InfoStatus("test22", this);
        s2.add(s21);
        s2.add(s22);
        s21.add(s211);

        Context context = new ContextBase();
        context.getStatusManager().add(s0);
        context.getStatusManager().add(s1);
        context.getStatusManager().add(s2);
        StatusPrinter.print(context);
        String result = outputStream.toString();
        assertTrue(result.contains("|-ERROR in " + this.getClass().getName()));
        assertTrue(result.contains("+ INFO in " + this.getClass().getName()));
        assertTrue(result.contains("com.dalcomlab.ap.logback.core.util.StatusPrinterTest.testWithException"));
    }

}
