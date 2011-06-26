/**
 * 
 */
package org.selunit.report.builder;

/**
 * Indicates an error of input builder.
 * 
 * @author mbok
 * 
 */
public class BuilderException extends Exception {

	private static final long serialVersionUID = -2363821086102073160L;

	public BuilderException(String message) {
		super(message);
	}

	public BuilderException(Throwable cause) {
		super(cause);
	}

	public BuilderException(String message, Throwable cause) {
		super(message, cause);
	}

}
