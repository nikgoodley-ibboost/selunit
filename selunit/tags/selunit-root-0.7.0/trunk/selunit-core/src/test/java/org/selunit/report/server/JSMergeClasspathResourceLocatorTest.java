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
