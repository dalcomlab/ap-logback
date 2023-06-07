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

import com.dalcomlab.ap.logback.core.rolling.RolloverFailure;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import com.dalcomlab.ap.logback.core.util.CoreTestConstants;
import com.dalcomlab.ap.logback.core.util.EnvUtil;
import com.dalcomlab.ap.logback.core.util.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileStoreUtilTest {

    int diff = RandomUtil.getPositiveInt();
    String pathPrefix = CoreTestConstants.OUTPUT_DIR_PREFIX + "fs" + diff + "/";

    @Test
    public void filesOnSameFolderShouldBeOnTheSameFileStore() throws RolloverFailure, IOException {
        if (!EnvUtil.isJDK7OrHigher())
            return;

        File parent = new File(pathPrefix);
        File file = new File(pathPrefix + "filesOnSameFolderShouldBeOnTheSameFileStore");
        FileUtil.createMissingParentDirectories(file);
        file.createNewFile();
        assertTrue(FileStoreUtil.areOnSameFileStore(parent, file));
    }

    // test should be run manually
    @Ignore
    @Test
    public void manual_filesOnDifferentVolumesShouldBeDetectedAsSuch() throws RolloverFailure {
        if (!EnvUtil.isJDK7OrHigher())
            return;

        // author's computer has two volumes
        File c = new File("c:/tmp/");
        File d = new File("d:/");
        assertFalse(FileStoreUtil.areOnSameFileStore(c, d));
    }
}
