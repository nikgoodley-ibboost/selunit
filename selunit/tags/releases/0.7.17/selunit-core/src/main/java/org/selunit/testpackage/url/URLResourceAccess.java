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
package org.selunit.testpackage.url;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.selunit.job.TestJob;
import org.selunit.testpackage.TestResource;
import org.selunit.testpackage.TestResourceAccess;


/**
 * Retrieves resources from a base URL ({@link #getBaseUrl()}. The resource URL
 * is calculated via this pattern: <baseURL>/<projectName>/<resourceName>.
 * 
 * @author mbok
 * 
 */
public class URLResourceAccess implements TestResourceAccess {
	private URL baseUrl;

	public static class URLResource implements TestResource {
		private byte[] content;
		private String name;
		private long length;

		/**
		 * @param content
		 * @param name
		 * @param length
		 */
		public URLResource(byte[] content, String name, long length) {
			super();
			this.content = content;
			this.name = name;
			this.length = length;
		}

		@Override
		public InputStream getContent() throws IOException {
			return new ByteArrayInputStream(content);
		}

		@Override
		public long getLength() {
			return length;
		}

		@Override
		public String getName() {
			return name;
		}

	}

	@Override
	public TestResource getResource(TestJob job, String name)
			throws IOException {
		URL url = new URL(getBaseUrl() + "/" + job.getProject().getName() + "/" + name);
		InputStream input = url.openConnection().getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[4096];
		while ((len = input.read(buffer)) >= 0) {
			if (len > 0) {
				out.write(buffer, 0, len);
			}
		}
		input.close();
		out.close();
		return new URLResource(out.toByteArray(), name, out.size());
	}

	/**
	 * @return the baseUrl
	 */
	public URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *            the baseUrl to set
	 */
	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

}
