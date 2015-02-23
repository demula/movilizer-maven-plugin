package com.movilizer.plugins.maven.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class MovilizerXMLParserServiceTest {

    private static final String testRequestDir = "/requests/";
    private static final String testEmptyRequestFilename = "ping-request.mxml";
    private static final String testMoveletRequestFilename = "create-movelet-request.mxml";
    private static final String testMoveletKey = "com.movilizer.plugins.maven.createMoveletTest";

    private MovilizerXMLParserService xmlParserService;

    @Before
    public void setUp() throws Exception {
        Log log = mock(Log.class);
        xmlParserService = new MovilizerXMLParserService(log, testRequestDir);
    }

    @Test
    public void testLoadEmptyRequest() throws Exception {
        File requestFile = new File(getClass().getResource(testRequestDir + testEmptyRequestFilename).toURI());
        MovilizerRequest request = xmlParserService.getRequestFromFile(requestFile);
        assertThat(request, is(notNullValue()));
    }

    @Test
    public void testLoadMoveletRequest() throws Exception {
        MovilizerRequest request = xmlParserService.getRequestFromFile(testRequestDir + testMoveletRequestFilename);
        assertThat(request, is(notNullValue()));
        assertThat(request.getMoveletSet().isEmpty(), is(false));
        assertThat(request.getMoveletSet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet(), is(notNullValue()));
        assertThat(request.getMoveletSet().get(0).getMovelet().size(), is(1));
        assertThat(request.getMoveletSet().get(0).getMovelet().get(0).getMoveletKey(), is(testMoveletKey));
    }
}
