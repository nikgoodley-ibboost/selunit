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
package org.selunit.config;

import java.util.Map;

import org.openqa.selenium.Capabilities;

/**
 * Wrapper for underlying capabilities map. All exposed methods have to access
 * the initial values from/to the associated {@link #getCapabilities()} map.
 * 
 * @author mbok
 * 
 */
public interface SeleniumProperties {

	public static final String KEY_BROWSER_URL = "browserUrl";
	public static final String KEY_TIMEOUT = "timeout";

	/**
	 * Returns the browser url.
	 * 
	 * @return browser url.
	 */
	public String getBrowserURL();

	/**
	 * Returns the timeout per suite. Default value is 360.
	 * 
	 * @return Returns the timeout per suite. Default value is 360.
	 */
	public int getTimeoutInSeconds();

	/**
	 * Capabilities as bridge to WebDriver's {@link Capabilities}.
	 * 
	 * @return additional browser capabilities
	 */
	public Map<String, ?> getCapabilities();

}
