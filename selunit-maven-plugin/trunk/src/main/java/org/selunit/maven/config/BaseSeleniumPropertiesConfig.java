package org.selunit.maven.config;

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

}
