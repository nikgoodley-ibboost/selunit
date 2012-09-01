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
package org.selunit.report;

/**
 * Represents the result type.
 * 
 * @author mbok
 * 
 */
public enum ResultType {
	/**
	 * Indicates successful test.
	 */
	PASSED("passed"),
	/**
	 * Indicates failed test.
	 */
	FAILED("failed"),
	/**
	 * Indicates a test canceled by user or system.
	 */
	CANCELED("canceled"),
	/**
	 * Indicates a currently running test.
	 */
	EXECUTING("executing"),
	
	/**
	 * Indicates a not run test.
	 */
	NOT_RUN("not-run");

	private String stringValue;

	ResultType(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}

};
