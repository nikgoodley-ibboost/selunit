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
