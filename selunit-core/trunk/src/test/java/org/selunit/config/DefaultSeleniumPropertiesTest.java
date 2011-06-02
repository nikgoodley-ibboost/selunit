package org.selunit.config;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.selunit.config.support.DefaultSeleniumProperties;


/**
 * Test for {@link DefaultSeleniumProperties}.
 * 
 * @author mbok
 * 
 */
public class DefaultSeleniumPropertiesTest {
	/**
	 * Tests {@link DefaultSeleniumProperties#getAsProperties()} method.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProperties() throws Exception {
		DefaultSeleniumProperties props = new DefaultSeleniumProperties();
		props.setBrowserKey("firefox");
		props.setBrowserURL("http://localhost:8080");
		props.setMultiWindow(true);
		props.setTimeoutInSeconds(123);
		Properties exportedProps = props.getAsProperties();
		Assert.assertEquals("firefox", exportedProps.get("browser"));
		Assert.assertEquals("http://localhost:8080", exportedProps
				.get("browser-url"));
		Assert.assertEquals("true", exportedProps.get("multi-window"));
		Assert.assertEquals("123", exportedProps.get("timeout"));
	}
}
