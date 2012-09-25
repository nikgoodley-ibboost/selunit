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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.selunit.testpackage.TestResource;


/**
 * Represents a file resource.
 * 
 * @author mbok
 * 
 */
public class FileResource implements TestResource {
	private File file;
	private String relativeFilePath;

	public FileResource(File file, String relativeFilePath) {
		this.file = file;
		this.relativeFilePath = relativeFilePath;
	}

	@Override
	public InputStream getContent() throws IOException {
		return new BufferedInputStream(new FileInputStream(file));
	}

	@Override
	public long getLength() {
		return file.length();
	}

	@Override
	public String getName() {
		return relativeFilePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
