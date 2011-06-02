package org.selunit.job.support;

import java.util.List;

import org.selunit.job.JobExecutorHandler;
import org.selunit.job.JobStatus;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.report.TestSuiteReport;


/**
 * Empty adapter for {@link JobExecutorHandler} interface.
 * 
 * @author mbok
 * 
 * @param <J>
 */
public class JobHandlerAdapter<J extends TestJob> implements
		JobExecutorHandler<J> {

	@Override
	public void startTestSuite(J job, String suite) throws TestJobException {
	}

	@Override
	public void finishTestSuite(J job, String suitePath, TestSuiteReport report)
			throws TestJobException {
	}

	@Override
	public void initJob(J job) throws TestJobException {
	}

	@Override
	public void startExecution(J job, List<String> suites)
			throws TestJobException {
	}

	@Override
	public void finishExecution(J job, JobStatus status)
			throws TestJobException {
	}

	@Override
	public void stopJob(J job) throws TestJobException {
	}

}
