package org.selunit.report.server;

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
