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
    @Parameter(property = "credentials.fill", defaultValue = "true")
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

    private MovilizerXMLParserService xmlParserService;
    private MovilizerWebService movilizerWebService;

    public RequestMojo() throws JAXBException {
        super();
        movilizerWebService = new MovilizerWebService(getLog(), webServiceAddress);
        xmlParserService = new MovilizerXMLParserService(getLog(), requestFolder);
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            MovilizerRequest request = xmlParserService.getRequestFromFile(requestFolder + requestFilename);
            if (!"false".equals(fillCredentials)) {
                request = movilizerWebService.prepareRequest(Long.parseLong(systemId), password, request);
            }
            MovilizerResponse response = movilizerWebService.getReplyFromCloud(request);
            if (movilizerWebService.responesHasErrors(response)) {
                getLog().error(movilizerWebService.prettyPrintErrors(response));
            } else {
                getLog().info(EN.SUCCESSFUL_REQUEST);
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
