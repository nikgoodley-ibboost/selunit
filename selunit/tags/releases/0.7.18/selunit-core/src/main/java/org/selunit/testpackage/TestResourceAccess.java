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
package org.selunit.testpackage;

import java.io.IOException;

import org.selunit.job.TestJob;


/**
 * A resource package represents an generic view to a container with resources.
 * This allows read test resources from file as well as from another location
 * like remote sites or database.
 * 
 * @author mbok
 * 
 */
public interface TestResourceAccess {
	/**
	 * Returns a resource from package with given path name. If resource doesn't
	 * exist null will be returned.
	 * 
	 * @param project
	 *            the appropriated job to return resource of
	 * 
	 * @param name
	 *            path name for resource inside the package.
	 * @return resource for given path name. Returns null if resource wasn't
	 *         found.
	 * @throws IOException
	 *             in cases of all IO errors
	 */
	public TestResource getResource(TestJob job, String name)
			throws IOException;
}
