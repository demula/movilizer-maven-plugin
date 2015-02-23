package com.movilizer.plugins.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays the plugin info to the user.
 */
@Mojo(name = "info")
public class InfoMojo extends AbstractMojo {
    private static final Logger logger = LoggerFactory.getLogger(InfoMojo.class);

    /**
     * The greeting to display.
     */
    @Parameter( property = "info.greeting", defaultValue = "Hello World!" )
    private String greeting;

    @Override
    public void execute() throws MojoExecutionException {
        logger.debug("Executing goal info from " + InfoMojo.class);
        getLog().info(greeting);
    }
}
