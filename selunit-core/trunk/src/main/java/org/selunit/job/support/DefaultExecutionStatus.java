package org.selunit.job.support;

import java.io.Serializable;

import org.selunit.job.JobStatus;
import org.selunit.job.TestJobException;


public class DefaultExecutionStatus implements JobStatus, Serializable {

	private static final long serialVersionUID = 3546384349668302232L;
	private StatusType type = StatusType.STOPPED;
	private TestJobException executionError;
	private boolean stoppedByUser = false;

	@Override
	public TestJobException getExecutionError() {
		return executionError;
	}

	/**
	 * @param executionError
	 *            the executionError to set
	 */
	public void setExecutionError(TestJobException executionError) {
		this.executionError = executionError;
	}

	@Override
	public StatusType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(StatusType type) {
		this.type = type;
	}

	/**
	 * @return the stoppedByUser
	 */
	@Override
	public boolean isStoppedByUser() {
		return stoppedByUser;
	}

	/**
	 * @param stoppedByUser
	 *            the stoppedByUser to set
	 */
	public void setStoppedByUser(boolean stoppedByUser) {
		this.stoppedByUser = stoppedByUser;
	}

}
