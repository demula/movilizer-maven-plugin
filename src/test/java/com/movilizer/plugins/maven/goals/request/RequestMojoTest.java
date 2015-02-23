package com.movilizer.plugins.maven.goals.request;

import com.movilizer.plugins.maven.InfoMojo;
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
public class RequestMojoTest extends AbstractMojoTestCase {
    private static final String TEST_POM_FILE_PATH = "src/test/resources/unit/basic-test/basic-test-plugin-config.xml";
    private static final String TEST_MAVEN_GOAL = "request";

    @Before
    public void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    @Test
    public void testMojoRequestGoal() throws Exception {
        File testPom = new File(getBasedir(), TEST_POM_FILE_PATH);
        InfoMojo mojo = (InfoMojo) lookupMojo(TEST_MAVEN_GOAL, testPom);
        assertThat(mojo, is(notNullValue()));
    }
}
