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
package org.selunit.rc.testpackage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.jetty.http.HttpContext;
import org.openqa.jetty.util.IO;
import org.openqa.jetty.util.Resource;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.rc.testpackage.TestResourceLocator;
import org.selunit.testpackage.file.DirectoryFileAccess;


/**
 * Test for {@link TestResourceLocator}.
 * 
 * @author mbok
 * 
 */
public class ResourcePackageLocatorTest {
	/**
	 * Tests {@link Resource} methods via {@link TestResourceLocator}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocator() throws Exception {
		TestResourceLocator locator = new TestResourceLocator(
				new DefaultTestJob(), new DirectoryFileAccess(new File(
						"src/test/resources")), "/myctx");
		Assert.assertFalse(locator.getResource(new HttpContext(), "missing")
				.exists());
		Assert.assertFalse(locator.getResource(new HttpContext(),
				"/other-ctx/SuiteSimple.html").exists());
		Resource r = locator.getResource(new HttpContext(),
				"/myctx/SuiteSimple.html");
		Assert.assertTrue(r.exists());
		long len;
		Assert.assertEquals((len = new File(
				"src/test/resources/SuiteSimple.html").length()), r.length());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		IO.copy(r.getInputStream(), buffer);
		Assert.assertEquals(len, buffer.size());
	}
}
