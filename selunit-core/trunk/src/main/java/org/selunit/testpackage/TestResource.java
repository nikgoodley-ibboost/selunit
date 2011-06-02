package org.selunit.testpackage;

import java.io.IOException;
import java.io.InputStream;

public interface TestResource {
	public InputStream getContent() throws IOException;

	public long getLength();

	public String getName();
}
