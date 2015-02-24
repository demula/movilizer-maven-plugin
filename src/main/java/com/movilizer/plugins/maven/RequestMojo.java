package com.movilizer.plugins.maven;

import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilizer.plugins.maven.messages.EN;
import com.movilizer.plugins.maven.services.MovilizerWebService;
import com.movilizer.plugins.maven.services.MovilizerXMLParserService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Performs a request to the Movilizer Cloud with the file given.
 */
@Mojo(name = "request")
public class RequestMojo extends AbstractMojo {

    /**
     * Request folder.
     */
    @Parameter(property = "requestFolder", defaultValue = DefaultValues.REQUEST_FOLDER)
    private String requestFolder;

    /**
     * File name of the request.
     */
    @Parameter(property = "defaultFilename", defaultValue = DefaultValues.REQUEST_FILE_NAME)
    private String requestFilename;

    /**
     * Web service address.
     */
    @Parameter(property = "webServiceAddress", defaultValue = DefaultValues.WEBSERVICE_ADDRESS)
    private String webServiceAddress;

    /**
     * Set system id and password from properties.
     */
    @Parameter(property = "credentials.fill", defaultValue = DefaultValues.FILL_CREDENTIALS)
    private String fillCredentials;

    /**
     * System id for the requests.
     */
    @Parameter(property = "credentials.systemId", defaultValue = "")
    private String systemId;

    /**
     * Password for the system id of the requests.
     */
    @Parameter(property = "credentials.password", defaultValue = "")
    private String password;

    /**
     * Debug mode to print replies in the console output.
     */
    @Parameter(property = "movilizer.debug", defaultValue = DefaultValues.DEBUG)
    private String debug;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            MovilizerWebService movilizerWebService = new MovilizerWebService(getLog(), webServiceAddress);
            MovilizerXMLParserService xmlParserService = new MovilizerXMLParserService(getLog(), requestFolder);
            MovilizerRequest request = xmlParserService.getRequestFromFile(requestFolder, requestFilename);
            if (Boolean.parseBoolean(fillCredentials)) {
                request = movilizerWebService.prepareRequest(Long.parseLong(systemId), password, request);
            }

            if (Boolean.parseBoolean(debug)) {
                getLog().info(xmlParserService.printRequest(request));
            }

            MovilizerResponse response = movilizerWebService.getReplyFromCloud(request);
            if (movilizerWebService.responesHasErrors(response)) {
                getLog().error(movilizerWebService.prettyPrintErrors(response));
            } else {
                getLog().info(EN.SUCCESSFUL_REQUEST);
            }

            if (Boolean.parseBoolean(debug)) {
                getLog().info(xmlParserService.printResponse(response));
            }

        } catch (IOException | URISyntaxException | JAXBException e) {
            throw rethrowException(e);
        }
    }

    private MojoExecutionException rethrowException(Exception e) {
        MojoExecutionException mojoEx = new MojoExecutionException(e.getMessage());
        mojoEx.initCause(e);
        return mojoEx;
    }
}
