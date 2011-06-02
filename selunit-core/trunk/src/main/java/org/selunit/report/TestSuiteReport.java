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
