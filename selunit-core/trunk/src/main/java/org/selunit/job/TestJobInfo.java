package org.selunit.job;

import org.selunit.TestProject;
import org.selunit.config.EnvironmentInfo;
import org.selunit.config.SeleniumProperties;

public interface TestJobInfo extends TestJobIdentifier {
	public String getName();

	public TestProject getProject();

	public EnvironmentInfo getEnvironment();

	public SeleniumProperties getSeleniumProperties();

}
