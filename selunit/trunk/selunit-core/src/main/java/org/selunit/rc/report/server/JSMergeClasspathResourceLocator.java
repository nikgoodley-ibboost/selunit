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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.jetty.http.HttpContext;
import org.openqa.jetty.util.Resource;
import org.openqa.selenium.server.ClasspathResourceLocator;

public class JSMergeClasspathResourceLocator extends ClasspathResourceLocator {

	private Map<String, MergedClassPathResource> mergeFiles = new HashMap<String, MergedClassPathResource>();

	public void addMergeJSResource(String originalResourceClassPath,
			String attachResourceClassPath) {
		this.mergeFiles.put(originalResourceClassPath,
				new MergedClassPathResource(originalResourceClassPath,
						attachResourceClassPath));
	}

	@Override
	public Resource getResource(HttpContext context, String pathInContext)
			throws IOException {
		if (mergeFiles.containsKey(pathInContext)) {
			Resource resource = mergeFiles.get(pathInContext);
			context.getResourceMetaData(resource);
			return resource;
		}
		return Resource.newResource("NOT RESPONSIBLE FOR THIS RESOURCE");
	}

}
