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
package com.dalcomlab.ap.logback.core.joran;

import com.dalcomlab.ap.logback.core.Context;
import com.dalcomlab.ap.logback.core.ContextBase;
import com.dalcomlab.ap.logback.core.CoreConstants;
import com.dalcomlab.ap.logback.core.joran.action.Action;
import com.dalcomlab.ap.logback.core.joran.action.ext.IncAction;
import com.dalcomlab.ap.logback.core.joran.spi.ElementSelector;
import com.dalcomlab.ap.logback.core.joran.spi.JoranException;
import com.dalcomlab.ap.logback.core.status.Status;
import com.dalcomlab.ap.logback.core.status.TrivialStatusListener;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import com.dalcomlab.ap.logback.core.util.CoreTestConstants;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.Assert.*;

public class TrivialConfiguratorTest {

    Context context = new ContextBase();
    HashMap<ElementSelector, Action> rulesMap = new HashMap<ElementSelector, Action>();

    public void doTest(String filename) throws Exception {

        // rule store is case insensitve
        rulesMap.put(new ElementSelector("x/inc"), new IncAction());

        TrivialConfigurator trivialConfigurator = new TrivialConfigurator(rulesMap);

        trivialConfigurator.setContext(context);
        trivialConfigurator.doConfigure(filename);
    }

    @Test
    public void smoke() throws Exception {
        int oldBeginCount = IncAction.beginCount;
        int oldEndCount = IncAction.endCount;
        int oldErrorCount = IncAction.errorCount;
        doTest(CoreTestConstants.TEST_SRC_PREFIX + "input/joran/" + "inc.xml");
        assertEquals(oldErrorCount, IncAction.errorCount);
        assertEquals(oldBeginCount + 1, IncAction.beginCount);
        assertEquals(oldEndCount + 1, IncAction.endCount);
    }

    @Test
    public void inexistentFile() {
        TrivialStatusListener tsl = new TrivialStatusListener();
        tsl.start();
        String filename = CoreTestConstants.TEST_SRC_PREFIX + "input/joran/" + "nothereBLAH.xml";
        context.getStatusManager().add(tsl);
        try {
            doTest(filename);
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Could not open ["));
        }
        assertTrue(tsl.list.size() + " should be greater than or equal to 1", tsl.list.size() >= 1);
        Status s0 = tsl.list.get(0);
        assertTrue(s0.getMessage().startsWith("Could not open ["));
    }

    @Test
    public void illFormedXML() {
        TrivialStatusListener tsl = new TrivialStatusListener();
        tsl.start();
        String filename = CoreTestConstants.TEST_SRC_PREFIX + "input/joran/" + "illformed.xml";
        context.getStatusManager().add(tsl);
        try {
            doTest(filename);
        } catch (Exception e) {
        }
        assertEquals(2, tsl.list.size());
        Status s0 = tsl.list.get(0);
        assertTrue(s0.getMessage().startsWith(CoreConstants.XML_PARSING));
    }

    @Test
    public void lbcore105() throws IOException, JoranException {
        String jarEntry = "buzz.xml";
        File jarFile = makeRandomJarFile();
        fillInJarFile(jarFile, jarEntry);
        URL url = asURL(jarFile, jarEntry);
        TrivialConfigurator tc = new TrivialConfigurator(rulesMap);
        tc.setContext(context);
        tc.doConfigure(url);
        // deleting an open file fails
        assertTrue(jarFile.delete());
        assertFalse(jarFile.exists());
    }

    @Test
    public void lbcore127() throws IOException, JoranException {
        String jarEntry = "buzz.xml";
        String jarEntry2 = "lightyear.xml";

        File jarFile = makeRandomJarFile();
        fillInJarFile(jarFile, jarEntry, jarEntry2);

        URL url1 = asURL(jarFile, jarEntry);
        URL url2 = asURL(jarFile, jarEntry2);

        URLConnection urlConnection2 = url2.openConnection();
        urlConnection2.setUseCaches(false);
        InputStream is = urlConnection2.getInputStream();

        TrivialConfigurator tc = new TrivialConfigurator(rulesMap);
        tc.setContext(context);
        tc.doConfigure(url1);

        is.read();
        is.close();

        // deleting an open file fails
        assertTrue(jarFile.delete());
        assertFalse(jarFile.exists());
    }

    File makeRandomJarFile() {
        File outputDir = new File(CoreTestConstants.OUTPUT_DIR_PREFIX);
        outputDir.mkdirs();
        int randomPart = RandomUtil.getPositiveInt();
        return new File(CoreTestConstants.OUTPUT_DIR_PREFIX + "foo-" + randomPart + ".jar");
    }

    private void fillInJarFile(File jarFile, String jarEntryName) throws IOException {
        fillInJarFile(jarFile, jarEntryName, null);
    }

    private void fillInJarFile(File jarFile, String jarEntryName1, String jarEntryName2) throws IOException {
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        jos.putNextEntry(new ZipEntry(jarEntryName1));
        jos.write("<x/>".getBytes());
        jos.closeEntry();
        if (jarEntryName2 != null) {
            jos.putNextEntry(new ZipEntry(jarEntryName2));
            jos.write("<y/>".getBytes());
            jos.closeEntry();
        }
        jos.close();
    }

    URL asURL(File jarFile, String jarEntryName) throws IOException {
        URL innerURL = jarFile.toURI().toURL();
        return new URL("jar:" + innerURL + "!/" + jarEntryName);
    }

}
