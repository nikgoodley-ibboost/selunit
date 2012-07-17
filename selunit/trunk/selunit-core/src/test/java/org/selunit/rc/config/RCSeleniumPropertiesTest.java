package org.selunit.rc.config;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test for {@link RCSeleniumProperties}.
 * 
 * @author mbok
 * 
 */
public class RCSeleniumPropertiesTest {
	@Test
	public void testGettersSetters() {
		RCSeleniumProperties rc = new RCSeleniumProperties();
		// Test defaults
		Assert.assertEquals(4449, rc.getPort());
		Assert.assertEquals(true, rc.isMultiWindow());
		Assert.assertNull(rc.getBrowserKey());
		Assert.assertNull(rc.getUserExtensions());

		// Set
		rc.setBrowserKey("*firefox");
		rc.setMultiWindow(false);
		rc.setPort(5999);
		rc.setUserExtensions("ext.js");

		// Check
		Assert.assertEquals(5999, rc.getPort());
		Assert.assertEquals(false, rc.isMultiWindow());
		Assert.assertEquals("*firefox", rc.getBrowserKey());
		Assert.assertEquals("ext.js", rc.getUserExtensions());

	}
}
