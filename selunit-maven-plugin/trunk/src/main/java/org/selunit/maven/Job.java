package org.selunit.maven;

import java.util.ArrayList;
import java.util.List;

import org.selunit.maven.config.EnvironmentInfoConfig;
import org.selunit.maven.config.SeleniumPropertiesConfig;

/**
 * Configuration bean for a test job.
 * 
 * @author mbok
 * 
 */
public class Job {
	private boolean includesSet, excludesSet;

	/**
	 * Test job name to output to reports.
	 * 
	 * @parameter default-value="${project.name}"
	 */
	private String name;

	/**
	 * Selenium properties used in this job execution. For more details see the
	 * <a href="SeleniumPropertiesConfig-mojo.html">SeleniumPropertiesConfig</a>
	 * bean.
	 * 
	 * @required
	 * @parameter
	 */
	private SeleniumPropertiesConfig seleniumProperties;

	/**
	 * Information about the test environment to add to reports. For more
	 * details see the <a
	 * href="EnvironmentInfoConfig-mojo.html">EnvironmentInfoConfig</a> bean.
	 * 
	 * @parameter
	 */
	private EnvironmentInfoConfig environmentInfo;

	/**
	 * A list of &lt;include>...&lt;/include> elements specifying the Selenium
	 * test suites (by pattern) that should be included in testing. When not
	 * specified, the default includes will be <code><br/>
	 * &lt;includeSuites><br/>
	 * &nbsp;&lt;include>**&#47;Suite*.html&lt;/include><br/>
	 * &nbsp;&lt;include>**&#47;*Suite.html&lt;/include><br/>
	 * &lt;/includeSuites><br/>
	 * </code>
	 * 
	 * @parameter
	 */
	private List<String> includeSuites;

	/**
	 * A list of &lt;exclude>...&lt;/exclude> elements specifying the Selenium
	 * test suites (by pattern) that should be excluded in testing. When not
	 * specified, the default excludes will be empty.
	 * 
	 * @parameter
	 */
	private List<String> excludeSuites;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the seleniumProperties
	 */
	public SeleniumPropertiesConfig getSeleniumProperties() {
		return seleniumProperties;
	}

	/**
	 * @param seleniumProperties
	 *            the seleniumProperties to set
	 */
	public void setSeleniumProperties(
			SeleniumPropertiesConfig seleniumProperties) {
		this.seleniumProperties = seleniumProperties;
	}

	/**
	 * @return the environmentInfo
	 */
	public EnvironmentInfoConfig getEnvironmentInfo() {
		return environmentInfo;
	}

	/**
	 * @param environmentInfo
	 *            the environmentInfo to set
	 */
	public void setEnvironmentInfo(EnvironmentInfoConfig environmentInfo) {
		this.environmentInfo = environmentInfo;
	}

	/**
	 * @return the includeSuites
	 */
	public List<String> getIncludeSuites() {
		if (includeSuites == null) {
			includeSuites = new ArrayList<String>();
			includeSuites.add("**/Suite*.html");
			includeSuites.add("**/*Suite.html");
		}
		return includeSuites;
	}

	/**
	 * @param includeSuites
	 *            the includeSuites to set
	 */
	public void setIncludeSuites(List<String> includeSuites) {
		this.includeSuites = includeSuites;
		includesSet = true;
	}

	/**
	 * @return the excludeSuites
	 */
	public List<String> getExcludeSuites() {
		if (excludeSuites == null) {
			excludeSuites = new ArrayList<String>();
		}
		return excludeSuites;
	}

	/**
	 * @param excludeSuites
	 *            the excludeSuites to set
	 */
	public void setExcludeSuites(List<String> excludeSuites) {
		this.excludeSuites = excludeSuites;
		excludesSet = true;
	}

	/**
	 * @return the includesSet
	 */
	public boolean isIncludesSet() {
		return includesSet;
	}

	/**
	 * @return the excludesSet
	 */
	public boolean isExcludesSet() {
		return excludesSet;
	}

}
