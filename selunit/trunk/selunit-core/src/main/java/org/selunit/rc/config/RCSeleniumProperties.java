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
package org.selunit.rc.config;

import org.selunit.config.SeleniumProperties;
import org.selunit.config.support.DefaultSeleniumProperties;

/**
 * Extensions of Selenium properties for the RC environment.
 * 
 * @author mbok
 * 
 */
public class RCSeleniumProperties extends DefaultSeleniumProperties {
	private static final long serialVersionUID = 7703080343953315313L;
	public static final String KEY_BROWSER_KEY = "browserKey";
	public static final String KEY_MULTI_WINDOW = "multiWindow";
	public static final String KEY_PORT = "port";
	public static final String KEY_USER_EXT = "userExtensions";

	/**
	 * Instantiates an empty instance with defaults.
	 */
	public RCSeleniumProperties() {
		super();
	}

	/**
	 * Instantiates an empty instance and derives values from given properties.
	 * 
	 * @param props
	 *            to copy property values from.
	 */
	public RCSeleniumProperties(SeleniumProperties props) {
		setCapabilities(props.getCapabilities());
	}

	public String getBrowserKey() {
		return getStrCapability(KEY_BROWSER_KEY, null);
	}

	public void setBrowserKey(String browserKey) {
		getCapabilities().put(KEY_BROWSER_KEY, browserKey);
	}

	public boolean isMultiWindow() {
		return "true".equals(getStrCapability(KEY_MULTI_WINDOW, "true"));
	}

	public void setMultiWindow(boolean multiWindow) {
		getCapabilities().put(KEY_MULTI_WINDOW, multiWindow);

	}

	/**
	 * Returns Selenium port. Default value is 4449.
	 * 
	 * @return Selenium port, default value is 4449.
	 */
	public int getPort() {
		return getIntCapability(KEY_PORT, 4449);
	}

	public void setPort(int port) {
		getCapabilities().put(KEY_PORT, port);
	}

	public String getUserExtensions() {
		return getStrCapability(KEY_USER_EXT, null);
	}

	public void setUserExtensions(String userExt) {
		getCapabilities().put(KEY_USER_EXT, userExt);
	}
}
