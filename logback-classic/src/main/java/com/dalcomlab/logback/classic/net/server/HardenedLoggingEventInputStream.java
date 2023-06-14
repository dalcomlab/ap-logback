package com.dalcomlab.logback.classic.net.server;

import com.dalcomlab.logback.classic.Level;
import com.dalcomlab.logback.classic.Logger;
import com.dalcomlab.logback.classic.spi.*;
import com.dalcomlab.logback.core.net.HardenedObjectInputStream;
import org.slf4j.helpers.BasicMarker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HardenedLoggingEventInputStream extends HardenedObjectInputStream {

    static final String ARRAY_PREFIX = "[L";
    
    static public List<String> getWhilelist() {
        List<String> whitelist = new ArrayList<String>();
        whitelist.add(LoggingEventVO.class.getName());
        whitelist.add(LoggerContextVO.class.getName());
        whitelist.add(LoggerRemoteView.class.getName());
        whitelist.add(ThrowableProxyVO.class.getName());
        whitelist.add(BasicMarker.class.getName());
        whitelist.add(Level.class.getName());
        whitelist.add(Logger.class.getName());
        whitelist.add(StackTraceElement.class.getName());
        whitelist.add(StackTraceElement[].class.getName());
        whitelist.add(ThrowableProxy.class.getName());
        whitelist.add(ThrowableProxy[].class.getName());
        whitelist.add(IThrowableProxy.class.getName());
        whitelist.add(IThrowableProxy[].class.getName());
        whitelist.add(StackTraceElementProxy.class.getName());
        whitelist.add(StackTraceElementProxy[].class.getName());
        whitelist.add(ClassPackagingData.class.getName());

        return whitelist;
    }
   
    public HardenedLoggingEventInputStream(InputStream is) throws IOException {
        super(is, getWhilelist());
    }
    
    public HardenedLoggingEventInputStream(InputStream is, List<String> additionalAuthorizedClasses) throws IOException {
        this(is);
        super.addToWhitelist(additionalAuthorizedClasses);
    }
}
