package org.selunit.webdriver;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.selunit.webdriver.support.DefaultExecutionContext;

/**
 * Provides some common test issues.
 * 
 * @author mbok
 * 
 */
public class WebdriverTestHelper {
	/**
	 * Creates a default execution context with {@link FirefoxDriver} without
	 * native events.
	 * 
	 * @return default execution context for tests
	 */
	public static DefaultExecutionContext createDefaultTestContext() {
		DefaultExecutionContext executionContext = new DefaultExecutionContext();
		executionContext.setBaseUrl(new File("src/test/resources").toURI()
				.toString());
		FirefoxProfile fp = new FirefoxProfile();
		fp.setEnableNativeEvents(false);
		executionContext.setWebDriver(new FirefoxDriver(fp));
		return executionContext;
	}
}
