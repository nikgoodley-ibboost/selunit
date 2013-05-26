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
package org.selunit.rc.report.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openqa.jetty.util.IO;
import org.openqa.jetty.util.Resource;
import org.openqa.selenium.server.ClassPathResource;

public class MergedClassPathResource extends ClassPathResource {
	private static final long serialVersionUID = 2341357140046726744L;
	private ByteArrayOutputStream mergedContent = new ByteArrayOutputStream();

	public MergedClassPathResource(String originalResourceClassPath,
			Resource... mergeResources) {
		super(originalResourceClassPath);
		try {
			IO.copy(super.getInputStream(), mergedContent);
			for (Resource r : mergeResources) {
				IO.copy(r.getInputStream(), mergedContent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(mergedContent.toByteArray());
	}

	@Override
	public long length() {
		return mergedContent.size();
	}

	@Override
	public boolean exists() {
		return true;
	}
}