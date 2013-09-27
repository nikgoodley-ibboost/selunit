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

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.server.RemoteControlConfiguration;

/**
 * Enhances remote control configuration by possibility to push additional
 * browser capabilities to the tests.
 * 
 * @author mbok
 * 
 */
public class ExtRemoteControlConfiguration extends RemoteControlConfiguration {
	private Capabilities browserCapabilities;

	@Override
	public Capabilities copySettingsIntoBrowserOptions(Capabilities source) {
		DesiredCapabilities thisCapabilities = new DesiredCapabilities(
				getBrowserCapabilities());
		thisCapabilities.merge(super.copySettingsIntoBrowserOptions(source));
		return thisCapabilities;
	}

	public Capabilities getBrowserCapabilities() {
		return browserCapabilities;
	}

	public void setBrowserCapabilities(Capabilities browserCapabilities) {
		this.browserCapabilities = browserCapabilities;
	}

}
