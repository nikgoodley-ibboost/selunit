package org.selunit.maven.docstubs;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.selunit.maven.Job;

/**
 * Configuration bean for a test job.
 * 
 * @goal Job
 */
public class Job4Doc extends Job implements Mojo {

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
