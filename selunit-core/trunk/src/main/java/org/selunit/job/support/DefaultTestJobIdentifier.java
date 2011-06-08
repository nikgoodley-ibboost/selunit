/*******************************************************************************
 * Copyright 2011 selunit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
