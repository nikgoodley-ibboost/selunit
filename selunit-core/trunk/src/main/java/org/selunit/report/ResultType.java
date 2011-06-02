package org.selunit.report;

/**
 * Represents the result type.
 * 
 * @author mbok
 * 
 */
public enum ResultType {
	/**
	 * Indicates successful test.
	 */
	PASSED("passed"),
	/**
	 * Indicates failed test.
	 */
	FAILED("failed"),
	/**
	 * Indicates a test canceled by user or system.
	 */
	CANCELED("canceled"),
	/**
	 * Indicates a currently running test.
	 */
	EXECUTING("executing");

	private String stringValue;

	ResultType(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}

};