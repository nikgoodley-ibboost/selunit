package org.selunit.report.server;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.jetty.http.HttpContext;
import org.openqa.jetty.util.Resource;
import org.openqa.selenium.server.ClassPathResource;
import org.selunit.report.server.JSMergeClasspathResourceLocator;


/**
 * Test for {@link JSMergeClasspathResourceLocator}.
 * 
 * @author mbok
 * 
 */
public class JSMergeClasspathResourceLocatorTest {

	@Test
	public void testMerging() throws Exception {
		JSMergeClasspathResourceLocator locator = new JSMergeClasspathResourceLocator();
		locator
				.addMergeJSResource("/SuiteSimple.html",
						"/test-selenium-logs.log");
		Assert.assertFalse(locator.getResource(new HttpContext(), "missing").exists());
		Resource mergedResource = locator.getResource(new HttpContext(), "/SuiteSimple.html");
		Assert.assertEquals(true, mergedResource.exists());
		Assert.assertEquals(new ClassPathResource("/SuiteSimple.html").length()
				+ new ClassPathResource("/test-selenium-logs.log").length(),
				mergedResource.length());
	}
}
