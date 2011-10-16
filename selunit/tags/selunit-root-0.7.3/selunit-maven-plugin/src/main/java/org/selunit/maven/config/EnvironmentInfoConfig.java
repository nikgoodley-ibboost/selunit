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
package org.selunit.maven.config;

/**
 * Configuration bean to provide information about the test environment.
 * 
 * @author mbok
 * 
 */
public class EnvironmentInfoConfig {
	/**
	 * Operating system description to add to reports.
	 * 
	 * @parameter default-value="${os.name} ${os.arch} ${os.version}"
	 */
	private String os;

	/**
	 * Browser description to add to reports. When not specified, the Selenium
	 * browser key without the "*" prefix is used.
	 * 
	 * @parameter
	 */
	private String browser;

	public EnvironmentInfoConfig() {
		super();
	}

	public EnvironmentInfoConfig(EnvironmentInfoConfig parent,
			EnvironmentInfoConfig child) {
		setOs(child.getOs() != null ? child.getOs() : parent.getOs());
		setBrowser(child.getBrowser() != null ? child.getBrowser() : parent
				.getBrowser());
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

}
