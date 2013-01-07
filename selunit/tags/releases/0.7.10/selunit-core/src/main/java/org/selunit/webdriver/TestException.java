package org.selunit.webdriver;

public class TestException extends RuntimeException {
	private static final long serialVersionUID = -3180521581881020188L;

	public TestException() {
		super();
	}

	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestException(String message) {
		super(message);
	}

	public TestException(Throwable cause) {
		super(cause);
	}
}
