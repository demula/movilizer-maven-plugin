package com.movilizer.plugins.maven;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class InfoMojoTest extends AbstractMojoTestCase {
    private static final Logger logger = LoggerFactory.getLogger(InfoMojoTest.class);
    private static final String TEST_POM_FILE_PATH = "src/test/resources/unit/basic-test/basic-test-plugin-config.xml";
    private static final String TEST_MAVEN_GOAL = "info";


    @Before
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
        logger.debug("InfoMojoTest setup complete");
    }

    @Test
    public void testMojoInfoGoal() throws Exception {
        logger.debug("Running testMojoInfoGoal()");
        File testPom = new File(getBasedir(), TEST_POM_FILE_PATH);
        InfoMojo mojo = (InfoMojo) lookupMojo(TEST_MAVEN_GOAL, testPom);
        assertThat(mojo, is(notNullValue()));
    }
}
