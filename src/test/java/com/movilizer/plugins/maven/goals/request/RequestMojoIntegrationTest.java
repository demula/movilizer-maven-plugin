package com.movilizer.plugins.maven.goals.request;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class RequestMojoIntegrationTest {
    private static final String TEST_DUMMY_PROJECT_FOLDER = "/dummy-projects/request";
    private static final String TEST_POM_FILE_PATH = "/dummy-projects/request/pom.xml";
    private static final String TEST_MAVEN_GOAL = "movilizer:request";


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testMojoInfoGoal() throws Exception {
        // Check in your dummy Maven project in /src/test/resources/...
        // The testdir is computed from the location of this
        // file.
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), TEST_DUMMY_PROJECT_FOLDER);

        Verifier verifier;

        /*
         * We must first make sure that any artifact created
         * by this test has been removed from the local
         * repository. Failing to do this could cause
         * unstable test results. Fortunately, the verifier
         * makes it easy to do this.
         */
        verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.deleteArtifact( "org.apache.maven.its.itsample", "parent", "1.0", "pom" );
        verifier.deleteArtifact( "org.apache.maven.its.itsample", "checkstyle-test", "1.0", "jar" );
        verifier.deleteArtifact( "org.apache.maven.its.itsample", "checkstyle-assembly", "1.0", "jar" );

        /*
         * The Command Line Options (CLI) are passed to the
         * verifier as a list. This is handy for things like
         * redefining the local repository if needed. In
         * this case, we use the -N flag so that Maven won't
         * recurse. We are only installing the parent pom to
         * the local repo here.
         */
        List<String> cliOptions = new ArrayList<>();
        cliOptions.add( "-N" );
        verifier.executeGoal(TEST_MAVEN_GOAL);

        /*
         * This is the simplest way to check a build
         * succeeded. It is also the simplest way to create
         * an IT test: make the build pass when the test
         * should pass, and make the build fail when the
         * test should fail. There are other methods
         * supported by the verifier. They can be seen here:
         * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
         */
        verifier.verifyErrorFreeLog();

        /*
         * Reset the streams before executing the verifier
         * again.
         */
        verifier.resetStreams();
//        assertThat(mojo, is(notNullValue()));
    }
}
