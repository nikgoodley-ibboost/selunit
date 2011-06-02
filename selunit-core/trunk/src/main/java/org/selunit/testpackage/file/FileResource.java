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
