package org.selunit.testpackage.file;

import java.io.File;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.testpackage.TestResource;
import org.selunit.testpackage.TestResourceAccess;
import org.selunit.testpackage.file.DirectoryFileAccess;
import org.selunit.testpackage.file.FileResource;


/**
 * Test for {@link FileResource} and {@link DirectoryFileAccess}.
 * 
 * @author mbok
 * 
 */
public class FileResourceTest {
	/**
	 * Tests methods of {@link TestResourceAccess}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPackage() throws Exception {
		TestResourceAccess p = new DirectoryFileAccess(new File(
				"src/test/resources"));
		DefaultTestJob job = new DefaultTestJob();
		Assert.assertNotNull(p.getResource(job, "SuiteSimple.html"));
		Assert.assertNull(p.getResource(job, "EOF.html"));
		Assert.assertNotNull(p.getResource(job,
				"testcases/GoogleSearch1.html"));
		Assert.assertNull(p.getResource(job,
				"testcases/GoogleSearch12345.html"));
		Assert.assertNotNull(p.getResource(job,
				"/testcases/GoogleSearch1.html"));
		Assert.assertEquals(
				p.getResource(job, "testcases/GoogleSearch1.html")
						.getLength(),
				p.getResource(job, "/testcases/GoogleSearch1.html")
						.getLength());
	}

	/**
	 * Tests methods of {@link TestResource}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResources() throws Exception {
		DirectoryFileAccess p = new DirectoryFileAccess(new File(
				"src/test/resources"));
		TestResource r = p.getResource(new DefaultTestJob(),
				"SuiteSimple.html");
		long len;
		Assert.assertEquals((len = new File(
				"src/test/resources/SuiteSimple.html").length()), r.getLength());
		Assert.assertEquals("SuiteSimple.html", r.getName());
		byte[] buffer = new byte[4096];
		InputStream is = r.getContent();
		int rLen;
		while ((rLen = is.read(buffer)) > 0) {
			len -= rLen;
		}
		is.close();
		Assert.assertEquals(0, len);
	}
}
