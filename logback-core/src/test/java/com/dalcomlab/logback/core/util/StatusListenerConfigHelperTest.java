package com.dalcomlab.logback.core.util;

import com.dalcomlab.logback.core.Context;
import com.dalcomlab.logback.core.ContextBase;
import com.dalcomlab.logback.core.status.OnConsoleStatusListener;
import com.dalcomlab.logback.core.status.StatusListener;
import com.dalcomlab.logback.core.status.StatusManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StatusListenerConfigHelperTest {

    Context context = new ContextBase();
    StatusManager sm = context.getStatusManager();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addOnConsoleListenerInstanceShouldNotStartSecondListener() {
        OnConsoleStatusListener ocl0 = new OnConsoleStatusListener();
        OnConsoleStatusListener ocl1 = new OnConsoleStatusListener();

        StatusListenerConfigHelper.addOnConsoleListenerInstance(context, ocl0);
        {
            List<StatusListener> listeners = sm.getCopyOfStatusListenerList();
            assertEquals(1, listeners.size());
            assertTrue(ocl0.isStarted());
        }

        // second listener should not have been started
        StatusListenerConfigHelper.addOnConsoleListenerInstance(context, ocl1);
        {
            List<StatusListener> listeners = sm.getCopyOfStatusListenerList();
            assertEquals(1, listeners.size());
            assertFalse(ocl1.isStarted());
        }
    }

}
