package org.selunit.report.server;

import org.openqa.selenium.server.htmlrunner.HTMLLauncher;

public class ExtHTMLLauncher extends HTMLLauncher implements
		ExtHTMLTestResultsListener {
	private ExtHTMLTestResults extTestResults;

	public ExtHTMLLauncher(ExtSeleniumServer remoteControl) {
		super(remoteControl);
	}

	public ExtHTMLTestResults getExtTestResults() {
		return extTestResults;
	}

	@Override
	public void processResults(ExtHTMLTestResults testResults) {
		this.extTestResults = testResults;
	}

}
