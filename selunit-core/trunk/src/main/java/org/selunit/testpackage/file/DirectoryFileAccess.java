package org.selunit.testpackage.file;

import java.io.File;
import java.io.IOException;

import org.selunit.job.TestJob;
import org.selunit.testpackage.TestResource;
import org.selunit.testpackage.TestResourceAccess;


/**
 * Represents a package view to a directory without considering the project
 * information.
 * 
 * @author mbok
 * 
 */
public class DirectoryFileAccess implements TestResourceAccess {
	private File directory;

	/**
	 * Creates a package view to given directory.
	 * 
	 * @param directory
	 *            the root for current package and all contained resources.
	 */
	public DirectoryFileAccess(File directory) {
		this.directory = directory;
	}

	@Override
	public TestResource getResource(TestJob job, String name)
			throws IOException {
		File f = new File(getDirectory(), name);
		if (f.exists()) {
			return new FileResource(f, name);
		} else {
			return null;
		}
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public void setDirectory(File directory) {
		this.directory = directory;
	}

}
