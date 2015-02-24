package com.movilizer.plugins.maven;

import com.movilizer.plugins.maven.messages.EN;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Displays the plugin info to the user.
 */
@Mojo(name = "info")
public class InfoMojo extends AbstractMojo {

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
     * Web service address.
     */
    @Parameter(property = "movilizer.webServiceAddress", defaultValue = DefaultValues.WEBSERVICE_ADDRESS)
    private String webServiceAddress;

    /**
     * Set system id and password from properties.
     */
    @Parameter(property = "movilizer.credentials.fill", defaultValue = DefaultValues.FILL_CREDENTIALS)
    private String fillCredentials;

    /**
     * System id for the requests.
     */
    @Parameter(property = "movilizer.credentials.systemId", defaultValue = "")
    private String systemId;

    /**
     * Password for the system id of the requests.
     */
    @Parameter(property = "movilizer.credentials.password", defaultValue = "")
    private String password;

    /**:
     * Debug mode to print replies in the console output.
     */
    @Parameter(property = "movilizer.debug", defaultValue = DefaultValues.DEBUG)
    private String debug;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info(String.format(EN.INFO_MESSAGE,
                webServiceAddress,
                systemId,
                password,
                fillCredentials,
                requestFolder,
                requestFilename,
                customizingFolder,
                debug));
    }
}
