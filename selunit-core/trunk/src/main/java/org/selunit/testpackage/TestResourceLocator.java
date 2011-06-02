package org.selunit.testpackage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.jetty.http.HttpContext;
import org.openqa.jetty.util.Resource;
import org.openqa.selenium.server.ResourceLocator;
import org.selunit.job.TestJob;


public class TestResourceLocator implements ResourceLocator {
	private TestResourceAccess resourceAccess;
	private String contextDirectory;
	private TestJob job;
	private boolean provideUserExtensions = false;

	public TestResourceLocator(TestJob job, TestResourceAccess resourceAccess,
			String contextDirectory) {
		this.resourceAccess = resourceAccess;
		this.contextDirectory = contextDirectory;
		this.job = job;
	}

	private class PackageResourceImpl extends Resource {
		private static final long serialVersionUID = -2874687785032937239L;
		private String packageRelativePath;
		private TestResource testResource;

		private PackageResourceImpl(String packageRelativePath)
				throws IOException {
			this.packageRelativePath = packageRelativePath;
			this.testResource = getResourceAccess().getResource(getJob(),
					packageRelativePath);
		}

		@Override
		public Resource addPath(String pathParm) throws IOException,
				MalformedURLException {
			return new PackageResourceImpl((new StringBuilder())
					.append(packageRelativePath).append("/").append(pathParm)
					.toString());
		}

		@Override
		public boolean delete() throws SecurityException {
			return false;
		}

		@Override
		public boolean exists() {
			return testResource != null;
		}

		@Override
		public File getFile() throws IOException {
			return null;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			if (testResource != null) {
				return testResource.getContent();
			} else {
				throw new IOException("Resource doesn't exist in package: "
						+ packageRelativePath);
			}
		}

		@Override
		public String getName() {
			return packageRelativePath;
		}

		@Override
		public OutputStream getOutputStream() throws IOException,
				SecurityException {
			return null;
		}

		@Override
		public URL getURL() {
			return null;
		}

		@Override
		public boolean isDirectory() {
			return false;
		}

		@Override
		public long lastModified() {
			return System.currentTimeMillis() + 31536000000L;
		}

		@Override
		public long length() {
			return testResource != null ? testResource.getLength() : 0;
		}

		@Override
		public String[] list() {
			return new String[0];
		}

		@Override
		public void release() {

		}

		@Override
		public boolean renameTo(Resource resource) throws SecurityException {
			return false;
		}

		@Override
		public String toString() {
			return getName();
		}

	}

	@Override
	public Resource getResource(HttpContext context, String pathInContext)
			throws IOException {
		if (pathInContext.startsWith(getContextDirectory())) {
			Resource resource = new PackageResourceImpl(
					pathInContext.substring(getContextDirectory().length()));
			context.getResourceMetaData(resource);
			return resource;
		} else if (isProvideUserExtensions()
				&& pathInContext.equals("/core/scripts/user-extensions.js")) {
			Resource resource = new PackageResourceImpl(getJob()
					.getSeleniumProperties().getUserExtensions());
			context.getResourceMetaData(resource);
			return resource;
		} else {
			return Resource.newResource("NOT RESPONSIBLE FOR THIS RESOURCE");
		}
	}

	/**
	 * @return the resourcePackage
	 */
	public TestResourceAccess getResourceAccess() {
		return resourceAccess;
	}

	/**
	 * @param resourcePackage
	 *            the resourcePackage to set
	 */
	public void setResourcePackage(TestResourceAccess resourceAccess) {
		this.resourceAccess = resourceAccess;
	}

	/**
	 * @return the contextDirectory
	 */
	public String getContextDirectory() {
		return contextDirectory;
	}

	/**
	 * @param contextDirectory
	 *            the contextDirectory to set
	 */
	public void setContextDirectory(String contextDirectory) {
		this.contextDirectory = contextDirectory;
	}

	/**
	 * @return the job
	 */
	public TestJob getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(TestJob job) {
		this.job = job;
	}

	/**
	 * @return the provideUserExtensions
	 */
	public boolean isProvideUserExtensions() {
		return provideUserExtensions;
	}

	/**
	 * @param provideUserExtensions
	 *            the provideUserExtensions to set
	 */
	public void setProvideUserExtensions(boolean provideUserExtensions) {
		this.provideUserExtensions = provideUserExtensions;
	}

}
