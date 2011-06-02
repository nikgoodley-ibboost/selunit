package org.selunit.report.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.jetty.http.HttpContext;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.ResourceLocator;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.StaticContentHandler;
import org.openqa.selenium.server.htmlrunner.HTMLResultsListener;

public class ExtSeleniumServer extends SeleniumServer {
	private static Log log = LogFactory.getLog(ExtSeleniumServer.class);

	private ExtHTMLTestResultsHandler extResultsHandler;

	private JSMergeClasspathResourceLocator jsClasspathResourceLocator;

	private StaticContentHandler staticContentHandler;

	public ExtSeleniumServer() throws Exception {
		super();
	}

	public ExtSeleniumServer(boolean slowResources,
			RemoteControlConfiguration configuration) throws Exception {
		super(slowResources, configuration);
	}

	public ExtSeleniumServer(boolean slowResources) throws Exception {
		super(slowResources);
	}

	public ExtSeleniumServer(RemoteControlConfiguration configuration)
			throws Exception {
		super(configuration);
	}

	@Override
	protected void createJettyServer(boolean slowResources) {
		super.createJettyServer(slowResources);
		extResultsHandler = new ExtHTMLTestResultsHandler();
		HttpContext seleniumContext = getServer()
				.getContext("/selenium-server");
		seleniumContext.addHandler(0, extResultsHandler);

		staticContentHandler = (StaticContentHandler) seleniumContext
				.getHandler(StaticContentHandler.class);
		if (!addStaticResourceLocator(getMergedJsClasspathResourceLocator())) {
			throw new RuntimeException(
					"StaticContentHandler not found in context. Report extensions can't be activated!");
		}
	}

	@Override
	public void handleHTMLRunnerResults(HTMLResultsListener listener) {
		super.handleHTMLRunnerResults(listener);
		if (listener instanceof ExtHTMLTestResultsListener) {
			extResultsHandler
					.addListener((ExtHTMLTestResultsListener) listener);
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
			jsClasspathResourceLocator.addMergeJSResource(
					"/core/scripts/selenium-testrunner.js",
					"/core/scripts/ext-selenium-testrunner.js");
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
			log
					.error("StaticContentHandler undefined, failed to add new resource locator: "
							+ locator);
			return false;
		}
	}

}
