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
package org.selunit.job;

import org.selunit.TestException;

public class TestJobException extends TestException {
	private static final long serialVersionUID = 3623215056440953332L;
	private TestJobIdentifier job;

	public TestJobException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestJobException(String message) {
		super(message);
	}

	public TestJobException(TestJobIdentifier job, String message, Throwable cause) {
		super(message, cause);
		this.job = job;
	}

	public TestJobException(TestJobIdentifier job, String message) {
		super(message);
		this.job = job;
	}

	/**
	 * @return the jobExecution
	 */
	public TestJobIdentifier getJob() {
		return job;
	}

	/**
	 * @param jobExecution
	 *            the jobExecution to set
	 */
	public void setJob(TestJobIdentifier job) {
		this.job = job;
	}

	@Override
	public String getMessage() {
		return (getJob() != null ? "[job=" + getJob() + "]: " : "")
				+ super.getMessage();
	}
}
