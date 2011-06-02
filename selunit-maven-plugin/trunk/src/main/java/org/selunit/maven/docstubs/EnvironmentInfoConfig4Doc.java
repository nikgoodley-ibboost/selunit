package org.selunit.maven.docstubs;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.selunit.maven.config.EnvironmentInfoConfig;

/**
 * Configuration bean to provide information about the test environment.
 * 
 * @goal EnvironmentInfoConfig
 */
public class EnvironmentInfoConfig4Doc extends EnvironmentInfoConfig implements
		Mojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Empty implementation, because used only for documentation creation
	}

	@Override
	public void setLog(Log log) {
		// Empty implementation, because used only for documentation creation
	}

	@Override
	public Log getLog() {
		// Empty implementation, because used only for documentation creation
		return null;
	}
}
