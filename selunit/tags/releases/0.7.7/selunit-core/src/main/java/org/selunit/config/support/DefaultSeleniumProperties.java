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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.selunit.config.SeleniumProperties;

/**
 * Default properties implementation. Note: All convenient methods have to
 * store/read values to/from {@link #getCapabilities()}.
 * 
 * @author mbok
 * 
 */
public class DefaultSeleniumProperties implements SeleniumProperties,
		Serializable {
	private static final long serialVersionUID = 3663780060638078002L;

	private Map<String, Object> capabilities = new HashMap<String, Object>();
	{
		setTimeoutInSeconds(360);
	}

	public DefaultSeleniumProperties() {
		super();
	}

	public DefaultSeleniumProperties(SeleniumProperties copy) {
		setCapabilities(copy.getCapabilities());
	}

	@Override
	public String toString() {
		return getCapabilities().toString();
	}

	@Override
	public String getBrowserURL() {
		return getStrCapability(KEY_BROWSER_URL, null);
	}

	@Override
	public int getTimeoutInSeconds() {
		return getIntCapability(KEY_TIMEOUT, 360);
	}

	/**
	 * @param browserURL
	 *            the browserURL to set
	 */
	public void setBrowserURL(String browserURL) {
		getCapabilities().put(SeleniumProperties.KEY_BROWSER_URL, browserURL);
	}

	/**
	 * @param timeoutInSeconds
	 *            the timeoutInSeconds to set
	 */
	public void setTimeoutInSeconds(int timeoutInSeconds) {
		getCapabilities().put(SeleniumProperties.KEY_TIMEOUT, timeoutInSeconds);
	}

	@Override
	public Map<String, Object> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Map<String, ?> capabilities) {
		this.capabilities = new HashMap<String, Object>(capabilities);
	}

	protected String getStrCapability(String key, String defaultValue) {
		Object v = getCapabilities().get(key);
		if (v != null) {
			return v.toString();
		} else {
			return defaultValue;
		}
	}

	protected int getIntCapability(String key, int defaultValue) {
		Object v = getCapabilities().get(key);
		if (v != null) {
			if (v instanceof Integer) {
				return (Integer) v;
			} else if (NumberUtils.isNumber(v.toString())) {
				return Integer.parseInt(v.toString());
			}
		}
		return defaultValue;
	}
}
