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
package org.selunit.config.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.selunit.config.SeleniumProperties;

public class DefaultSeleniumProperties implements SeleniumProperties,
		Serializable {
	private static final long serialVersionUID = 3663780060638078002L;

	private static Log log = LogFactory.getLog(DefaultSeleniumProperties.class);

	private String browserKey, browserURL, userExtensions;
	private int timeoutInSeconds = 30, port = 4449;
	private boolean multiWindow = true;
	private Map<String, ?> browserCapabilities;

	public DefaultSeleniumProperties() {
		super();
	}

	public DefaultSeleniumProperties(SeleniumProperties copy) {
		this.browserKey = copy.getBrowserKey();
		this.browserURL = copy.getBrowserURL();
		this.timeoutInSeconds = copy.getTimeoutInSeconds();
		this.multiWindow = copy.isMultiWindow();
		this.userExtensions = copy.getUserExtensions();
	}

	@Override
	public String getBrowserKey() {
		return browserKey;
	}

	public void setBrowserKey(String browserKey) {
		this.browserKey = browserKey;
	}

	@Override
	public String toString() {
		return getAsProperties().toString();
	}

	@Override
	public String getBrowserURL() {
		return browserURL;
	}

	@Override
	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	@Override
	public boolean isMultiWindow() {
		return multiWindow;
	}

	/**
	 * @param browserURL
	 *            the browserURL to set
	 */
	public void setBrowserURL(String browserURL) {
		this.browserURL = browserURL;
	}

	/**
	 * @param timeoutInSeconds
	 *            the timeoutInSeconds to set
	 */
	public void setTimeoutInSeconds(int timeoutInSeconds) {
		this.timeoutInSeconds = timeoutInSeconds;
	}

	/**
	 * @param multiWindow
	 *            the multiWindow to set
	 */
	public void setMultiWindow(boolean multiWindow) {
		this.multiWindow = multiWindow;
	}

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
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public Map<String, ?> getBrowserCapabilities() {
		return browserCapabilities;
	}

	public void setBrowserCapabilities(Map<String, ?> browserCapabilities) {
		this.browserCapabilities = browserCapabilities;
	}

	@Override
	public Properties getAsProperties() {
		Properties props = new Properties();
		for (Method m : SeleniumProperties.class.getMethods()) {
			if (m.isAnnotationPresent(MapToProperty.class)) {
				String name = m.getAnnotation(MapToProperty.class).name();
				try {
					props.setProperty(name, m.invoke(this) + "");
				} catch (Exception e) {
					log.error("Can't access selenium property: " + name, e);
				}
			}
		}
		if (getBrowserCapabilities() != null) {
			for (String k : getBrowserCapabilities().keySet()) {
				props.setProperty(k, getBrowserCapabilities().get(k).toString());
			}
		}
		return props;
	}

}
