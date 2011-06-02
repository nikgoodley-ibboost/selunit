package org.selunit;

/**
 * General exception for test execution.
 * 
 * @author mbok
 * 
 */
public class TestException extends Exception {

	private static final long serialVersionUID = -7618918366267261515L;

	public TestException(String arg0) {
		super(arg0);
	}

	public TestException(Throwable arg0) {
		super(arg0);
	}

	public TestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
