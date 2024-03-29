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
package org.selunit.testpackage.url;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.support.DefaultTestProject;
import org.selunit.testpackage.TestResource;
import org.selunit.testpackage.url.URLResourceAccess;


/**
 * Test for {@link URLResourceAccess}.
 * 
 * @author mbok
 * 
 */
public class URLResourceAccessTest {
	/**
	 * Test with existent resources.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExistentResource() throws Exception {
		String basePath = new File("src/test").getAbsolutePath().replace("\\",
				"/");
		if (basePath.contains(":")) {
			basePath = basePath.substring(basePath.indexOf(":") + 1);
		}
		URLResourceAccess access = new URLResourceAccess();
		access.setBaseUrl(new URL("file://" + basePath));
		DefaultTestProject proj = new DefaultTestProject();
		proj.setName("resources");
		DefaultTestJob job = new DefaultTestJob();
		job.setProject(proj);
		TestResource suite = access.getResource(job, "SuiteSimple.html");
		Assert.assertNotNull(suite);
		Assert.assertEquals("SuiteSimple.html", suite.getName());
		Assert.assertEquals(
				new File("src/test/resources/SuiteSimple.html").length(),
				suite.getLength());

		// Change hierarchy
		access.setBaseUrl(new URL("file://"
				+ basePath.substring(0, basePath.length() - 5)));
		proj.setName("test");
		suite = access.getResource(job, "resources/SuiteSimple.html");
		Assert.assertNotNull(suite);
		Assert.assertEquals("resources/SuiteSimple.html", suite.getName());
		Assert.assertEquals(
				new File("src/test/resources/SuiteSimple.html").length(),
				suite.getLength());
	}

	/**
	 * Test with a not existent resource.
	 * 
	 * @throws Exception
	 */
	@Test(expected = IOException.class)
	public void testNotExistentResources() throws Exception {
		URLResourceAccess access = new URLResourceAccess();
		access.setBaseUrl(new URL("file://root"));
		DefaultTestProject proj = new DefaultTestProject();
		proj.setName("null");
		DefaultTestJob job = new DefaultTestJob();
		job.setProject(proj);
		access.getResource(job, "none.txt");
	}
}
