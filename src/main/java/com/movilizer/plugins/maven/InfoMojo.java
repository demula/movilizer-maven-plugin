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
        getLog().info(String.format(EN.INFO_MESSAGE,
                webServiceAddress,
                systemId,
                password,
                fillCredentials,
                requestFolder,
                requestFilename,
                debug));
    }
}
