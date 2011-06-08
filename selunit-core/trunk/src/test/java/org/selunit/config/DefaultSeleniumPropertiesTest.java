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
