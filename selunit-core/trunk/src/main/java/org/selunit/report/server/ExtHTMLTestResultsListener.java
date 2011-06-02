package org.selunit.report.server;

import org.openqa.selenium.server.htmlrunner.HTMLResultsListener;

public interface ExtHTMLTestResultsListener extends HTMLResultsListener {
	public void processResults(ExtHTMLTestResults testResults);
}
