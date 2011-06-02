package org.selunit.job;

import java.util.List;

public interface JobExecutor<J extends TestJob> {
	public void addHandler(JobExecutorHandler<J> handler);

	public void removeHandler(JobExecutorHandler<J> handler);

	public List<JobExecutorHandler<J>> getHandlers();

	public JobStatus getStatus();

	public void init(J job) throws TestJobException;

	public void start(List<String> suites) throws TestJobException;

	public void stop(boolean shutdown) throws TestJobException;

}