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

import java.util.List;

import org.selunit.report.TestSuiteReport;


public interface JobExecutorHandler<J extends TestJob> {

	public void startTestSuite(J job, String suite) throws TestJobException;

	public void finishTestSuite(J job, String suitePath, TestSuiteReport report)
			throws TestJobException;

	public void initJob(J job) throws TestJobException;

	public void stopJob(J job) throws TestJobException;

	public void startExecution(J job, List<String> suites)
			throws TestJobException;

	public void finishExecution(J job, JobStatus status)
			throws TestJobException;
}
