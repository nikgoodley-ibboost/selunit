package org.selunit.job;

import org.selunit.TestException;

public class TestJobException extends TestException {
	private static final long serialVersionUID = 3623215056440953332L;
	private TestJobIdentifier job;

	public TestJobException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestJobException(String message) {
		super(message);
	}

	public TestJobException(TestJobIdentifier job, String message, Throwable cause) {
		super(message, cause);
		this.job = job;
	}

	public TestJobException(TestJobIdentifier job, String message) {
		super(message);
		this.job = job;
	}

	/**
	 * @return the jobExecution
	 */
	public TestJobIdentifier getJob() {
		return job;
	}

	/**
	 * @param jobExecution
	 *            the jobExecution to set
	 */
	public void setJob(TestJobIdentifier job) {
		this.job = job;
	}

	@Override
	public String getMessage() {
		return (getJob() != null ? "[job=" + getJob() + "]: " : "")
				+ super.getMessage();
	}
}
