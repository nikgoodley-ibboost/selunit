package org.selunit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.selunit.report.server.ExtSeleniumServer;


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
	protected RemoteControlConfiguration getServerConfiguration() {
		RemoteControlConfiguration conf = new RemoteControlConfiguration();
		conf.setPort(4449);
		conf.setSingleWindow(false);
		conf.setTimeoutInSeconds(11111111);
		// conf.setUserExtensions(new
		// File("src/test/resources/user-extensions.js"));
		return conf;
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
