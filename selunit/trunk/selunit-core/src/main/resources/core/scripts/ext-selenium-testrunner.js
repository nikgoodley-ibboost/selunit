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