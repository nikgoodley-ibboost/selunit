package org.selunit.support;

import java.io.Serializable;

import org.selunit.TestProject;


/**
 * Default bean for a test project.
 * 
 * @author mbok
 * 
 */
public class DefaultTestProject implements TestProject, Serializable {

	private static final long serialVersionUID = -5147564577864542551L;
	private String id, name;

	public DefaultTestProject() {
		super();
	}

	public DefaultTestProject(TestProject copy) {
		this.id = copy.getId();
		this.name = copy.getName();
	}

	/**
	 * @return the id
	 */
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
