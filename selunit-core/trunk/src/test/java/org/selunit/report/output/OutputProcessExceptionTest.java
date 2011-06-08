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
package org.selunit.report.output;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.selunit.report.output.OutputProcessException;
import org.selunit.report.support.DefaultTestSuite;


/**
 * Test for {@link OutputProcessException}.
 * 
 * @author mbok
 * 
 */
public class OutputProcessExceptionTest {

	/**
	 * Tests {@link OutputProcessException#getMessage()} with set suite.
	 */
	@Test
	public void testGetMessageWithSuite() {
		DefaultTestSuite suite = new DefaultTestSuite();
		suite.setName("test");
		OutputProcessException e = new OutputProcessException(suite,
				"test exception");
		Assert.assertEquals("[suite=" + suite + "]: " + "test exception",
				e.getMessage());
		e = new OutputProcessException(suite, "test exception",
				new IOException());
		Assert.assertEquals("[suite=" + suite + "]: " + "test exception",
				e.getMessage());
	}

	/**
	 * Tests {@link OutputProcessException#getMessage()} without set suite.
	 */
	@Test
	public void testGetMessageWithoutSuite() {
		DefaultTestSuite suite = new DefaultTestSuite();
		suite.setName("test");
		OutputProcessException e = new OutputProcessException("test exception");
		Assert.assertEquals("test exception", e.getMessage());
		e = new OutputProcessException("test exception", new IOException());
		Assert.assertEquals("test exception", e.getMessage());
	}
}
