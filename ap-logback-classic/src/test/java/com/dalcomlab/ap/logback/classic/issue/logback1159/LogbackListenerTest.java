package com.dalcomlab.ap.logback.classic.issue.logback1159;

import com.dalcomlab.ap.logback.classic.LoggerContext;
import com.dalcomlab.ap.logback.classic.joran.JoranConfigurator;
import com.dalcomlab.ap.logback.core.joran.spi.JoranException;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinderFriend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.Set;

public class LogbackListenerTest {
    private File logFile = new File("target/test.log");

    private void doConfigure() throws JoranException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure(new File("src/test/input/issue/logback-1159.xml"));
    }

    @After
    public void after() {
        logFile.delete();
        StaticLoggerBinderFriend.reset();
    }

    private void disableLogFileAccess() throws IOException {
        logFile.createNewFile();
        logFile.deleteOnExit();
        Path path = Paths.get(logFile.toURI());
        Set<PosixFilePermission> permissions = Collections.emptySet();
        try {
            Files.setPosixFilePermissions(path, permissions);
        } catch (UnsupportedOperationException e) {
            path.toFile().setReadOnly();
        }
    }

    @Test(expected = LoggingError.class)
    public void testThatErrorIsDetectedAtLogInit() throws Exception {
        disableLogFileAccess();
        doConfigure();
    }

    @Test
    public void assertThatNonFailSafeAppendersNotAffected() throws JoranException {
        doConfigure();
        Logger logger = LoggerFactory.getLogger("NOTJOURNAL");
        logger.error("This should not fail");
    }

}