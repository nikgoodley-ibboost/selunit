package org.selunit.job.support;

import java.io.Serializable;

import org.selunit.job.TestJobIdentifier;


public class DefaultTestJobIdentifier implements TestJobIdentifier,
		Serializable {
	private static final long serialVersionUID = -3366024376901879436L;
	private String id;

	/**
	 * Creates an empty identifier.
	 */
	public DefaultTestJobIdentifier() {
		super();
	}

	/**
	 * Creates an empty identifier.
	 */
	public DefaultTestJobIdentifier(String id) {
		this.id = id;
	}

	/**
	 * Creates a new identifier as copy from given job.
	 * 
	 * @param copy
	 */
	public DefaultTestJobIdentifier(TestJobIdentifier copy) {
		this(copy.getId());
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getId();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TestJobIdentifier && getId().equals(
				((TestJobIdentifier) obj).getId()));
	}
}
