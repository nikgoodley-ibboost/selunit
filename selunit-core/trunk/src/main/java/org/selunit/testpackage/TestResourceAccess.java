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
