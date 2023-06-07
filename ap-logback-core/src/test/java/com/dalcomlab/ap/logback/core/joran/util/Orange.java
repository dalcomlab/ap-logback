package com.dalcomlab.ap.logback.core.joran.util;

public class Orange extends Citrus<Integer> {

    public void setPericarp(Integer pericarp) {
        System.out.println("Orange.setPericarp");
        super.setPericarp(pericarp);
    }

    @Override
    public void foo() {
    }
}
