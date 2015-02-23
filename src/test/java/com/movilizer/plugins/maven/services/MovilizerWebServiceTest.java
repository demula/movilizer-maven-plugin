package com.movilizer.plugins.maven.services;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.plugins.maven.DefaultValues;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class MovilizerWebServiceTest {
    private static long SYSTEM_ID = 1L; //Put your own here
    private static String PASSWORD = "pass"; //Put your own here

    private MovilizerWebService webService;

    @Before
    public void setUp() throws Exception {
        Log log = mock(Log.class);
        webService = new MovilizerWebService(log, DefaultValues.WEBSERVICE_ADDRESS);
    }

    @Ignore
    @Test
    public void testPerformEmptyRequest() throws Exception {
        MovilizerRequest request = new MovilizerRequest();
        request = webService.prepareRequest(SYSTEM_ID, PASSWORD, request);
        MovilizerResponse response = webService.getReplyFromCloud(request);
        assertThat(response, is(notNullValue()));
        assertThat(webService.responesHasErrors(response), is(false));
    }
}
