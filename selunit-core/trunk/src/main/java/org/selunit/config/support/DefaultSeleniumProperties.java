package org.selunit.config.support;

import java.io.Serializable;
import java.lang.reflect.Method;
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
		return props;
	}

}
