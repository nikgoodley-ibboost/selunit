package org.selunit.job;

public interface JobStatus {
	public enum StatusType {
		INITIALIZING, INITIALIZED, EXECUTING_SUITES, FINISHED_SUITES, STOPPING, STOPPED
	};

	public TestJobException getExecutionError();

	public StatusType getType();

	public boolean isStoppedByUser();
}
