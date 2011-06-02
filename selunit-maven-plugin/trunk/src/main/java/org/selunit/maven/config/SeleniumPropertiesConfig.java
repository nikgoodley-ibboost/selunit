package org.selunit.maven.config;

/**
 * Configuration bean to define Selenium properties used in a test job
 * execution.
 * 
 * @author mbok
 * 
 */
public class SeleniumPropertiesConfig extends BaseSeleniumPropertiesConfig {
	private boolean unsetPort = true, unsetBrowserUrl = true;

	/**
	 * The port on which the Selenium server will be started.
	 * 
	 * @parameter default-value="4449"
	 */
	private int port = 4449;

	/**
	 * The Selenium browser key on which the test will be run; must be one of
	 * the standard valid browser names (and must start with a *), e.g.
	 * *firefox, *iexplore, *custom.
	 * 
	 * @required
	 * @parameter default-value="*firefox"
	 */
	private String browserKey = "*firefox";

	public SeleniumPropertiesConfig() {
		super();
	}

	/**
	 * Creates a new instance by merging parameters from parent and child.
	 * 
	 * @param parent
	 * @param child
	 */
	public SeleniumPropertiesConfig(SeleniumPropertiesConfig parent,
			SeleniumPropertiesConfig child) {
		super(parent, child);
		setPort(!child.unsetPort ? child.getPort() : parent.getPort());
		setBrowserKey(!child.unsetBrowserUrl ? child.getBrowserKey() : parent
				.getBrowserKey());
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
		this.unsetPort = port == 0;
	}

	/**
	 * @return the browserKey
	 */
	public String getBrowserKey() {
		return browserKey;
	}

	/**
	 * @param browserKey
	 *            the browserKey to set
	 */
	public void setBrowserKey(String browserKey) {
		this.browserKey = browserKey;
		this.unsetBrowserUrl = browserKey == null;
	}

}
