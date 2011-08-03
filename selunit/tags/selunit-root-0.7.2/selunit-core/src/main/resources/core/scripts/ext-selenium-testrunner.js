/**
 * Copyright 2011 selunit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** START REPORTING EXTENSIONS * */

HtmlTestSuite.prototype._onTestSuiteComplete = function() {
	// <--START-CHANGES--
	new ExtSeleniumTestResult().post();
	// --END-CHANGES-->
	this.markDone();
	new SeleniumTestResult(this.failed, this.getTestTable()).post();
}

var ExtSeleniumTestResult = classCreate();
objectExtend(ExtSeleniumTestResult.prototype, {

	// Post the extended results to the extended result handler
	// bound to "/postExtResults".
	//
	// Parameters passed to the results-handler are:
	// result: passed/failed depending on whether the suite passed or failed
	// totalTime: the total running time in seconds for the suite.
	//
	// numTestPasses: the total number of tests which passed.
	// numTestFailures: the total number of tests which failed.
	//
	// numCommandPasses: the total number of commands which passed.
	// numCommandFailures: the total number of commands which failed.
	// numCommandErrors: the total number of commands which errored.
	//
	// suite: the suite table, including the hidden column of test results
	// testTable.1 to testTable.N: the individual test tables
	//
	initialize : function() {
		this.controlPanel = htmlTestRunner.controlPanel;
	},

	post : function() {
		if (!this.controlPanel.isAutomatedRun()) {
			return;
		}
		var form = document.createElement("form");
		document.body.appendChild(form);

		form.id = "extResultsForm";
		form.method = "post";
		form.target = "selenium_myiframe";

		form.action = "/selenium-server/postExtResults";

		form.createHiddenField = function(name, value) {
			input = document.createElement("input");
			input.type = "hidden";
			input.name = name;
			input.value = value;
			this.appendChild(input);
		};

		form.createHiddenField("selenium.version", Selenium.version);
		form.submit();
		document.body.removeChild(form);
	}
});

HtmlRunnerTestLoop.prototype.initialize = function(htmlTestCase, metrics,
		seleniumCommandFactory) {
	// <--START-CHANGES--
	LOG.info('Start test case "' + htmlTestCase.pathname + '" at: '
			+ (new Date().getTime()));
	// --END-CHANGES-->
	this.commandFactory = new HtmlRunnerCommandFactory(seleniumCommandFactory,
			this);
	this.metrics = metrics;

	this.htmlTestCase = htmlTestCase;
	LOG.info("Starting test " + htmlTestCase.pathname);

	this.currentRow = null;
	this.currentRowIndex = 0;

	// used for selenium tests in javascript
	this.currentItem = null;
	this.commandAgenda = new Array();
	this.expectedFailure = null;
	this.expectedFailureType = null;

	this.htmlTestCase.reset();
};

HtmlTestRunner.prototype.runNextTest = function() {
	// <--START-CHANGES--
	if (this.currentTest) {
		LOG.info('Finished test case "'
				+ testFrame.getCurrentTestCase().pathname + '" at: '
				+ (new Date().getTime()));
	}
	// --END-CHANGES-->
	this.getTestSuite().updateSuiteWithResultOfPreviousTest();
	if (!this.runAllTests) {
		return;
	}
	this.getTestSuite().runNextTestInSuite();
};

/** END REPORTING EXTENSIONS **/