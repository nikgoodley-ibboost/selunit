package org.selunit.job.support;

import java.util.List;

import org.selunit.TestProject;
import org.selunit.config.EnvironmentInfo;
import org.selunit.config.SeleniumProperties;
import org.selunit.job.TestJob;


/**
 * Default bean for a test job.
 * 
 * @author mbok
 * 
 */
public class DefaultTestJob extends DefaultTestJobIdentifier implements TestJob {
	private static final long serialVersionUID = 8765034831459772848L;

	private String name;
	private TestProject project;
	private SeleniumProperties seleniumProperties;
	private List<String> suites;
	private EnvironmentInfo environment;

	/**
	 * Creates an empty job.
	 */
	public DefaultTestJob() {
		super();
	}

	/**
	 * Creates a new job as copy from given job.
	 * 
	 * @param copy
	 */
	public DefaultTestJob(TestJob copy) {
		super(copy);
		setName(copy.getName());
		setEnvironment(copy.getEnvironment());
		setSeleniumProperties(copy.getSeleniumProperties());
		setSuites(copy.getSuites());
		setProject(copy.getProject());
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the project
	 */
	public TestProject getProject() {
		return project;
	}

	@Override
	public SeleniumProperties getSeleniumProperties() {
		return seleniumProperties;
	}

	@Override
	public List<String> getSuites() {
		return suites;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(TestProject project) {
		this.project = project;
	}

	/**
	 * @param seleniumProperties
	 *            the seleniumProperties to set
	 */
	public void setSeleniumProperties(SeleniumProperties seleniumProperties) {
		this.seleniumProperties = seleniumProperties;
	}

	/**
	 * @param suites
	 *            the suites to set
	 */
	public void setSuites(List<String> suites) {
		this.suites = suites;
	}

	/**
	 * @return the environment
	 */
	public EnvironmentInfo getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(EnvironmentInfo environment) {
		this.environment = environment;
	}

}
