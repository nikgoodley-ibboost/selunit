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
package org.selunit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Capabilities;
import org.selunit.config.support.ExtRemoteControlConfiguration;
import org.selunit.rc.report.server.ExtSeleniumServer;

/**
 * Super class for test cases working with a selenium server instance
 * {@link #getServer()}, which is started before and ended after tests.
 * 
 * @author mbok
 * 
 */
public abstract class ServerLauncherTest {
	private ExtSeleniumServer server;

	/**
	 * Override to manipulate server configuration.
	 * 
	 * @return Used server configuration.
	 */
	protected ExtRemoteControlConfiguration getServerConfiguration() {
		ExtRemoteControlConfiguration conf = new ExtRemoteControlConfiguration();
		conf.setPort(4449);
		conf.setSingleWindow(false);
		conf.setTimeoutInSeconds(11111111);
		conf.setBrowserCapabilities(getCapabilities());
		// conf.setUserExtensions(new
		// File("src/test/resources/user-extensions.js"));
		return conf;
	}

	/**
	 * Overridable in super classes.
	 * 
	 * @return capabilities to use in {@link #getServerConfiguration()}.
	 */
	protected Capabilities getCapabilities() {
		return null;
	}

	/**
	 * Returns started selenium server instance.
	 * 
	 * @return Started selenium server instance.
	 */
	protected ExtSeleniumServer getServer() {
		return server;
	}

	/**
	 * Starts selenium server.
	 * 
	 * @throws Exception
	 */
	@Before
	public void startServer() throws Exception {
		server = new ExtSeleniumServer(false, getServerConfiguration());
		server.start();
	}

	/**
	 * Stops selenium server.
	 * 
	 * @throws Exception
	 */
	@After
	public void stopServer() throws Exception {
		server.stop();
	}
}
