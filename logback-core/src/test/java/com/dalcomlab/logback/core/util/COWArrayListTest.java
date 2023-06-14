package com.dalcomlab.logback.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class COWArrayListTest {

    Integer[] model = new Integer[0];
    COWArrayList<Integer> cowaList = new COWArrayList<Integer>(model);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void basicToArray() {
        cowaList.add(1);
        Object[] result = cowaList.toArray();
        assertArrayEquals(new Integer[] { 1 }, result);
    }

    @Test
    public void basicToArrayWithModel() {
        cowaList.add(1);
        Integer[] result = cowaList.toArray(model);
        assertArrayEquals(new Integer[] { 1 }, result);
    }

    
    @Test
    public void basicToArrayTyped() {
        cowaList.add(1);
        Integer[] result = cowaList.asTypedArray();
        assertArrayEquals(new Integer[] { 1 }, result);
    }

}
