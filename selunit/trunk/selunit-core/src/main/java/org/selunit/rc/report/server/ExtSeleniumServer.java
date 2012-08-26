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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.jetty.http.HttpContext;
import org.openqa.selenium.server.ClassPathResource;
import org.openqa.selenium.server.ResourceLocator;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.StaticContentHandler;
import org.selunit.config.support.ExtRemoteControlConfiguration;
import org.selunit.rc.config.CapabilitiesResource;

public class ExtSeleniumServer extends SeleniumServer {
	private static Log log = LogFactory.getLog(ExtSeleniumServer.class);

	private JSMergeClasspathResourceLocator jsClasspathResourceLocator;

	private StaticContentHandler staticContentHandler;

	public ExtSeleniumServer(boolean slowResources,
			ExtRemoteControlConfiguration configuration) throws Exception {
		super(slowResources, configuration);
	}

	public ExtSeleniumServer(ExtRemoteControlConfiguration configuration)
			throws Exception {
		super(configuration);
	}

	@Override
	protected void createJettyServer(boolean slowResources) {
		super.createJettyServer(slowResources);
		HttpContext seleniumContext = getServer()
				.getContext("/selenium-server");

		staticContentHandler = (StaticContentHandler) seleniumContext
				.getHandler(StaticContentHandler.class);
		if (!addStaticResourceLocator(getMergedJsClasspathResourceLocator())) {
			throw new RuntimeException(
					"StaticContentHandler not found in context. Report extensions can't be activated!");
		}
	}

	/**
	 * Returns a locator instance for merged JS files.
	 * 
	 * @return Locator instance for merged JS files.
	 */
	public JSMergeClasspathResourceLocator getMergedJsClasspathResourceLocator() {
		if (jsClasspathResourceLocator == null) {
			jsClasspathResourceLocator = new JSMergeClasspathResourceLocator();
			jsClasspathResourceLocator
					.addMergeJSResource(
							"/core/scripts/selenium-testrunner.js",
							new CapabilitiesResource(
									((ExtRemoteControlConfiguration) getConfiguration())
											.getBrowserCapabilities()),
							new ClassPathResource(
									"/core/scripts/ext-selenium-testrunner.js"));
		}
		return jsClasspathResourceLocator;
	}

	/**
	 * Adds a new resource locator to static content handler of this server.
	 * 
	 * @param locator
	 *            new resource locator to add
	 * @return true, if extension was successful, otherwise false
	 */
	public boolean addStaticResourceLocator(ResourceLocator locator) {
		if (staticContentHandler != null) {
			staticContentHandler.addStaticContent(locator);
			return true;
		} else {
			log.error("StaticContentHandler undefined, failed to add new resource locator: "
					+ locator);
			return false;
		}
	}

}
