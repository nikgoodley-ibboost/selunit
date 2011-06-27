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

import org.selunit.report.TestSuiteReport;

/**
 * Indicates an error during the output process of a suite report.
 * 
 * @author mbok
 * 
 */
public class OutputProcessException extends Exception {
	private TestSuiteReport suite;

	private static final long serialVersionUID = -294785287447766117L;

	public OutputProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutputProcessException(String message) {
		super(message);
	}

	public OutputProcessException(TestSuiteReport suite, String message,
			Throwable cause) {
		super(message, cause);
		this.suite = suite;
	}

	public OutputProcessException(TestSuiteReport suite, String message) {
		super(message);
		this.suite = suite;
	}

	public TestSuiteReport getSuite() {
		return suite;
	}

	public void setSuite(TestSuiteReport suite) {
		this.suite = suite;
	}

	@Override
	public String getMessage() {
		return (getSuite() != null ? "[suite=" + getSuite() + "]: " : "")
				+ super.getMessage();
	}
}
