package org.selunit.maven.config;

/**
 * Configuration bean to provide information about the test environment.
 * 
 * @author mbok
 * 
 */
public class EnvironmentInfoConfig {
	/**
	 * Operating system description to add to reports.
	 * 
	 * @parameter default-value="${os.name} ${os.arch} ${os.version}"
	 */
	private String os;

	/**
	 * Browser description to add to reports. When not specified, the Selenium
	 * browser key without the "*" prefix is used.
	 * 
	 * @parameter
	 */
	private String browser;

	public EnvironmentInfoConfig() {
		super();
	}

	public EnvironmentInfoConfig(EnvironmentInfoConfig parent,
			EnvironmentInfoConfig child) {
		setOs(child.getOs() != null ? child.getOs() : parent.getOs());
		setBrowser(child.getBrowser() != null ? child.getBrowser() : parent
				.getBrowser());
	}

	/**
	 * @return the os
	 */
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
