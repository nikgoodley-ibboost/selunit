package org.selunit.report;

public interface TestCaseReport {
	
	public String getName();

	public String getFileName();

	public double getTime();

	public ResultType getResultType();

	public TestReportLog getResultLog();

	public long getStartTime();

	public long getEndTime();
}
