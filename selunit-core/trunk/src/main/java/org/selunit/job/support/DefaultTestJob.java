/*******************************************************************************
 * Copyright 2011 selunit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
