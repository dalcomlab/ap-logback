package com.dalcomlab.ap.logback.core.rolling;

import com.dalcomlab.ap.logback.core.Context;
import com.dalcomlab.ap.logback.core.ContextBase;
import com.dalcomlab.ap.logback.core.FileAppender;
import com.dalcomlab.ap.logback.core.encoder.NopEncoder;
import com.dalcomlab.ap.logback.core.status.Status;
import com.dalcomlab.ap.logback.core.status.StatusChecker;
import com.dalcomlab.ap.logback.core.testUtil.RandomUtil;
import com.dalcomlab.ap.logback.core.util.StatusPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.dalcomlab.ap.logback.core.CoreConstants.FA_FILENAME_COLLISION_MAP;
import static com.dalcomlab.ap.logback.core.util.CoreTestConstants.OUTPUT_DIR_PREFIX;

public class CollisionDetectionTest {

    Context context = new ContextBase();
    StatusChecker statusChecker = new StatusChecker(context);
    int diff = RandomUtil.getPositiveInt();
    protected String randomOutputDir = OUTPUT_DIR_PREFIX + diff + "/";
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    
    FileAppender<String> buildFileAppender(String name, String filenameSuffix) {
        FileAppender<String> fileAppender = new FileAppender<String>();
        fileAppender.setName(name);
        fileAppender.setContext(context);
        fileAppender.setFile(randomOutputDir+filenameSuffix);
        fileAppender.setEncoder(new NopEncoder<String>());
        return fileAppender;
    }
    
    RollingFileAppender<String> buildRollingFileAppender(String name, String filenameSuffix, String patternSuffix) {
        RollingFileAppender<String> rollingFileAppender = new RollingFileAppender<String>();
        rollingFileAppender.setName(name);
        rollingFileAppender.setContext(context);
        rollingFileAppender.setFile(randomOutputDir+filenameSuffix);
        rollingFileAppender.setEncoder(new NopEncoder<String>());
        
        TimeBasedRollingPolicy<String> tbrp = new TimeBasedRollingPolicy<String>();
        tbrp.setContext(context);
        tbrp.setFileNamePattern(randomOutputDir+patternSuffix);
        tbrp.setParent(rollingFileAppender);
        //tbrp.timeBasedFileNamingAndTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<Object>();
        //tbrp.timeBasedFileNamingAndTriggeringPolicy.setCurrentTime(givenTime);
        rollingFileAppender.setRollingPolicy(tbrp);
        tbrp.start();

        
        return rollingFileAppender;
    }
    
    
    @Test
    public void collisionImpossibleForSingleAppender() {
        FileAppender<String> fileAppender = buildFileAppender("FA", "collisionImpossibleForSingleAppender");
        fileAppender.start();
        statusChecker.assertIsErrorFree();
        
    }

    @Test
    public void appenderStopShouldClearEntryInCollisionMap() {
        String key = "FA";
        FileAppender<String> fileAppender = buildFileAppender(key, "collisionImpossibleForSingleAppender");
        fileAppender.start();
        assertCollisionMapHasEntry(FA_FILENAME_COLLISION_MAP, key);
        fileAppender.stop();
        assertCollisionMapHasNoEntry(FA_FILENAME_COLLISION_MAP, key);
        statusChecker.assertIsErrorFree();
        
        
    }
    
    private void assertCollisionMapHasEntry(String mapName, String key) {
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) context.getObject(mapName);
        Assert.assertNotNull(map);
        Assert.assertNotNull(map.get(key));
    }
    private void assertCollisionMapHasNoEntry(String mapName, String key) {
        @SuppressWarnings("unchecked")
        Map<String, ?> map = (Map<String, ?>) context.getObject(mapName);
        Assert.assertNotNull(map);
        Assert.assertNull(map.get(key));
    }

    @Test
    public void collisionWithTwoFileAppenders() {
        String suffix = "collisionWithToFileAppenders";
        
        FileAppender<String> fileAppender1 = buildFileAppender("FA1", suffix);
        fileAppender1.start();
        
        FileAppender<String> fileAppender2 = buildFileAppender("FA2", suffix);
        fileAppender2.start();
        statusChecker.assertContainsMatch(Status.ERROR, "'File' option has the same value");
        //StatusPrinter.print(context);
    }

    @Test
    public void collisionWith_FA_RFA() {
        String suffix = "collisionWith_FA_RFA";
        
        FileAppender<String> fileAppender1 = buildFileAppender("FA", suffix);
        fileAppender1.start();
        
        RollingFileAppender<String> rollingfileAppender = buildRollingFileAppender("RFA", suffix, "bla-%d.log");
        rollingfileAppender.start();
        StatusPrinter.print(context);
        statusChecker.assertContainsMatch(Status.ERROR, "'File' option has the same value");        
    }

    @Test
    public void collisionWith_2RFA() {
        String suffix = "collisionWith_2RFA";
        
        RollingFileAppender<String> rollingfileAppender1 = buildRollingFileAppender("RFA1", suffix, "bla-%d.log");
        rollingfileAppender1.start();
        RollingFileAppender<String> rollingfileAppender2 = buildRollingFileAppender("RFA1", suffix, "bla-%d.log");
        rollingfileAppender2.start();
        
        StatusPrinter.print(context);
        statusChecker.assertContainsMatch(Status.ERROR, "'FileNamePattern' option has the same value");        
    }

}
