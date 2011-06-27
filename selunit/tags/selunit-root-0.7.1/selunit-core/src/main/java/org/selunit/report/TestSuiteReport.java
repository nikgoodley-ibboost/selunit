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

import java.util.List;

import org.selunit.job.TestJobInfo;


public interface TestSuiteReport {
	public String getName();

	public String getFileName();

	public double getTime();

	public TestJobInfo getJobInfo();

	public List<TestCaseReport> getTestCases();

	public long getStartTime();

	public long getEndTime();

	public ResultType getResultType();
	
	public int getIterationCount();
}