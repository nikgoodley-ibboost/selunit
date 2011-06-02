package org.selunit.report.output;

import org.selunit.report.TestSuiteReport;

/**
 * Indicates an error during the output process of a suite report.
 * 
 * @author mbok
 * 
 */
public class OutputProcessException extends Exception {
	private TestSuiteReport suite;

	private static final long serialVersionUID = -294785287447766117L;

	public OutputProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutputProcessException(String message) {
		super(message);
	}

	public OutputProcessException(TestSuiteReport suite, String message,
			Throwable cause) {
		super(message, cause);
		this.suite = suite;
	}

	public OutputProcessException(TestSuiteReport suite, String message) {
		super(message);
		this.suite = suite;
	}

	public TestSuiteReport getSuite() {
		return suite;
	}

	public void setSuite(TestSuiteReport suite) {
		this.suite = suite;
	}

	@Override
	public String getMessage() {
		return (getSuite() != null ? "[suite=" + getSuite() + "]: " : "")
				+ super.getMessage();
	}
}
