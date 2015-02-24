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
 * Creates a request file that contains all icons and customizing options from a theme folder.
 */
@Mojo(name = "customizing")
public class CustomizingMojo extends AbstractMojo {

    /**
     * Request folder.
     */
    @Parameter(property = "movilizer.requestFolder", defaultValue = DefaultValues.REQUEST_FOLDER)
    private String requestFolder;

    /**
     * File name of the request.
     */
    @Parameter(property = "movilizer.filename", defaultValue = DefaultValues.REQUEST_FILE_NAME)
    private String requestFilename;

    /**
     * File name of the request.
     */
    @Parameter(property = "movilizer.customizingFolder", defaultValue = DefaultValues.CUSTOMIZING_FOLDER)
    private String customizingFolder;

    /**
     * Debug mode to print replies in the console output.
     */
    @Parameter(property = "movilizer.debug", defaultValue = DefaultValues.DEBUG)
    private String debug;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            MovilizerXMLParserService xmlParserService = new MovilizerXMLParserService(getLog(), requestFolder);
            MovilizerRequest request = xmlParserService.getRequestFromFile(requestFolder, requestFilename);

            if (Boolean.parseBoolean(debug)) {
                getLog().info(xmlParserService.printRequest(request));
            }
            xmlParserService.saveRequestToFile(request, requestFilename);
        } catch (IOException | JAXBException e) {
            throw rethrowException(e);
        }
    }

    private MojoExecutionException rethrowException(Exception e) {
        MojoExecutionException mojoEx = new MojoExecutionException(e.getMessage());
        mojoEx.initCause(e);
        return mojoEx;
    }
}
