package org.selunit.webdriver;

public class InvalidCommandException extends Exception {
	private static final long serialVersionUID = -1639227890327155063L;

	public InvalidCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCommandException(String message) {
		super(message);
	}

}
