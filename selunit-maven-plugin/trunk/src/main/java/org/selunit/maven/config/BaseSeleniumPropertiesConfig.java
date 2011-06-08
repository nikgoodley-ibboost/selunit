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

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration bean to define base Selenium properties used in a test job
 * execution.
 * 
 * @author mbok
 * 
 */
public class BaseSeleniumPropertiesConfig {
	public BaseSeleniumPropertiesConfig() {
		super();
	}

	public BaseSeleniumPropertiesConfig(BaseSeleniumPropertiesConfig parent,
			BaseSeleniumPropertiesConfig child) {
		setBrowserUrl(child.getBrowserUrl() != null ? child.getBrowserUrl()
				: parent.getBrowserUrl());
		setUserExtensions(child.getUserExtensions() != null ? child
				.getUserExtensions() : parent.getUserExtensions());
		setTimeoutInSeconds(!child.unsetTimeoutInSeconds ? child
				.getTimeoutInSeconds() : parent.getTimeoutInSeconds());
		setMultiWindow(!child.unsetMultiWindow ? child.isMultiWindow() : parent
				.isMultiWindow());
		if (child.getBrowserCapabilities() != null
				&& parent.getBrowserCapabilities() != null) {
			Map<String, String> merge = new HashMap<String, String>();
			merge.putAll(parent.getBrowserCapabilities());
			merge.putAll(child.getBrowserCapabilities());
			setBrowserCapabilities(merge);
		} else {
			setBrowserCapabilities(child.getBrowserCapabilities() != null ? child
					.getBrowserCapabilities() : parent.getBrowserCapabilities());
		}
	}

	private boolean unsetTimeoutInSeconds = true, unsetMultiWindow = true;

	/**
	 * The user extensions JavaScript file to add to Selenium runtime
	 * environment. This file has to be located in the configured test resources
	 * directory.
	 * 
	 * @parameter
	 */
	private String userExtensions;

	/**
	 * True if the application under test should run in its own window, false if
	 * the AUT will run in an embedded iframe.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean multiWindow = true;

	/**
	 * The base URL on which the tests will be run, e.g. http://www.google.com.
	 * Note that only the host name part of this URL will really be used.
	 * 
	 * @parameter
	 * @required
	 */
	private String browserUrl;

	/**
	 * Amount of time to wait before killing the browser.
	 * 
	 * @parameter default-value="1800"
	 */
	private int timeoutInSeconds = 1800;

	/**
	 * Additional browser capabilities according to the WebDriver
	 * <code>Capabilities</code> concepts to pass special aspects to the
	 * browser. An example is to disable web security in Google Chrome by:
	 * <code><br/>
	 * &lt;browserCapabilities><br/>
	 * &nbsp;&lt;commandLineFlags>--disable-web-security&lt;/commandLineFlags><br/>
	 * &lt;/browserCapabilities><br/>
	 * </code>
	 * 
	 * @parameter
	 */
	private Map<String, String> browserCapabilities;

	/**
	 * @return the userExtensions
	 */
	public String getUserExtensions() {
		return userExtensions;
	}

	/**
	 * @param userExtensions
	 *            the userExtensions to set
	 */
	public void setUserExtensions(String userExtensions) {
		this.userExtensions = userExtensions;
	}

	/**
	 * @return the multiWindow
	 */
	public boolean isMultiWindow() {
		return multiWindow;
	}

	/**
	 * @param multiWindow
	 *            the multiWindow to set
	 */
	public void setMultiWindow(boolean multiWindow) {
		this.multiWindow = multiWindow;
		this.unsetMultiWindow = false;
	}

	/**
	 * @return the browserUrl
	 */
	public String getBrowserUrl() {
		return browserUrl;
	}

	/**
	 * @param browserUrl
	 *            the browserUrl to set
	 */
	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}

	/**
	 * @return the timeoutInSeconds
	 */
	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	/**
	 * @param timeoutInSeconds
	 *            the timeoutInSeconds to set
	 */
	public void setTimeoutInSeconds(int timeoutInSeconds) {
		this.timeoutInSeconds = timeoutInSeconds;
		this.unsetTimeoutInSeconds = false;
	}

	public Map<String, String> getBrowserCapabilities() {
		return browserCapabilities;
	}

	public void setBrowserCapabilities(Map<String, String> browserCapabilities) {
		this.browserCapabilities = browserCapabilities;
	}

}
