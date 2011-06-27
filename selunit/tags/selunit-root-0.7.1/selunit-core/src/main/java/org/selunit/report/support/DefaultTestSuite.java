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
package org.selunit.report.support;

import java.io.Serializable;
import java.util.List;

import org.selunit.job.TestJobInfo;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestSuiteReport;


/**
 * Default bean for a suite report. The initialized result type is
 * {@link ResultType#EXECUTING}.
 * 
 * @author mbok
 * 
 */
public class DefaultTestSuite implements TestSuiteReport, Serializable {
	private static final long serialVersionUID = -3604847684180175466L;
	private String name;
	private double time;
	private List<TestCaseReport> testCases;
	private TestJobInfo jobInfo;
	private long startTime, endTime;
	private ResultType resultType = ResultType.EXECUTING;
	private String fileName;
	private int iterationCount = 1;

	public DefaultTestSuite() {
		super();
	}

	public DefaultTestSuite(TestSuiteReport copy) {
		this.name = copy.getName();
		this.time = copy.getTime();
		this.testCases = copy.getTestCases();
		this.startTime = copy.getStartTime();
		this.endTime = copy.getEndTime();
		this.jobInfo = copy.getJobInfo();
		this.resultType = copy.getResultType();
		this.fileName = copy.getFileName();
		this.iterationCount = copy.getIterationCount();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public List<TestCaseReport> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCaseReport> testCases) {
		this.testCases = testCases;
	}

	@Override
	public TestJobInfo getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(TestJobInfo jobInfo) {
		this.jobInfo = jobInfo;
	}

	/**
	 * @return the startTime
	 */
	@Override
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	@Override
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the resultType
	 */
	@Override
	public ResultType getResultType() {
		return resultType;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	/**
	 * @return the fileName
	 */
	@Override
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the iterationCount
	 */
	@Override
	public int getIterationCount() {
		return iterationCount;
	}

	/**
	 * @param iterationCount
	 *            the iterationCount to set
	 */
	public void setIterationCount(int iterationCount) {
		this.iterationCount = iterationCount;
	}

	@Override
	public String toString() {
		return "filename=" + getFileName() + "; name=" + getName() + ";";
	}
}
