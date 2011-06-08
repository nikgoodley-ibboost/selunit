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

import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestReportLog;


public class DefaultTestCase implements TestCaseReport, Serializable {
	private static final long serialVersionUID = 1425513081555678875L;
	private String name;
	private String fileName;
	private double time;
	private ResultType resultType;
	private TestReportLog resultLog;
	private long startTime, endTime;

	public DefaultTestCase() {
		super();
	}

	public DefaultTestCase(TestCaseReport copy) {
		this.name = copy.getName();
		this.fileName = copy.getFileName();
		this.time = copy.getTime();
		this.resultType = copy.getResultType();
		this.resultLog = copy.getResultLog();
		this.startTime = copy.getStartTime();
		this.endTime = copy.getEndTime();
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	@Override
	public TestReportLog getResultLog() {
		return resultLog;
	}

	public void setResultLog(TestReportLog resultLog) {
		this.resultLog = resultLog;
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

	@Override
	public String toString() {
		return "name=" + getName() + "; file=" + getFileName() + "; result="
				+ getResultType() + "; time=" + getTime() + "; log=["
				+ getResultLog() + "];";
	}
}
