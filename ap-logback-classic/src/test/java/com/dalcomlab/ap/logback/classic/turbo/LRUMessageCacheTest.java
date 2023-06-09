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
package com.dalcomlab.ap.logback.classic.turbo;

import com.dalcomlab.ap.logback.classic.turbo.LRUMessageCache;
import junit.framework.Assert;
import org.junit.Test;

public class LRUMessageCacheTest {

    @Test
    public void testEldestEntriesRemoval() {
        final LRUMessageCache cache = new LRUMessageCache(2);
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("0"));
        Assert.assertEquals(1, cache.getMessageCountAndThenIncrement("0"));
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("1"));
        Assert.assertEquals(1, cache.getMessageCountAndThenIncrement("1"));
        // 0 entry should have been removed.
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("2"));
        // So it is expected a returned value of 0 instead of 2.
        // 1 entry should have been removed.
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("0"));
        // So it is expected a returned value of 0 instead of 2.
        // 2 entry should have been removed.
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("1"));
        // So it is expected a returned value of 0 instead of 2.
        Assert.assertEquals(0, cache.getMessageCountAndThenIncrement("2"));
    }

}
