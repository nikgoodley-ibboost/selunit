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
package org.selunit.rc.report.server;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.jetty.util.IO;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.selunit.ServerLauncherTest;
import org.selunit.rc.report.server.ExtSeleniumServer;

/**
 * Tests {@link ExtSeleniumServer}.
 * 
 * @author mbok
 * 
 */
public class ExtSeleniumServerTest extends ServerLauncherTest {

	/**
	 * Tests merging of /core/scripts/selenium-testrunner.js.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergingSeleniumTestrunnerJs() throws Exception {
		URL extSeleniumTestrunner = new URL("http://localhost:"
				+ getServer().getPort()
				+ "/selenium-server/core/scripts/selenium-testrunner.js");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IO.copy(extSeleniumTestrunner.openConnection().getInputStream(), out);
		String strOut = new String(out.toByteArray());
		String jsLines[] = strOut.trim().split("\\n");
		Assert.assertEquals("/** END REPORTING EXTENSIONS **/",
				jsLines[jsLines.length - 1]);
		Assert.assertEquals(true, strOut.contains("Selenium.capabilities"));
		Assert.assertEquals(true, strOut.contains("myCapAttr"));
		Assert.assertEquals(true, strOut.contains("myCapValue"));
	}

	@Override
	protected Capabilities getCapabilities() {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("myCapAttr", "myCapValue");
		return caps;
	}

}
