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
