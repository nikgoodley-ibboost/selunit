package org.selunit.config.support;

import java.io.Serializable;

import org.selunit.config.EnvironmentInfo;


/**
 * Default bean for environment information.
 * 
 * @author mbok
 * 
 */
public class DefaultEnvironmentInfo implements EnvironmentInfo, Serializable {
	private static final long serialVersionUID = 7906343865464349478L;
	private String os, browser;

	public DefaultEnvironmentInfo() {
		super();
	}

	public DefaultEnvironmentInfo(String os, String browser) {
		this.os = os;
		this.browser = browser;
	}

	/**
	 * @return the os
	 */
	@Override
	public String getOs() {
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @return the browser
	 */
	@Override
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

}
