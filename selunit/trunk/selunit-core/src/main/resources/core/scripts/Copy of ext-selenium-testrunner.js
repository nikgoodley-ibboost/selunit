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

HtmlTestRunner.prototype.markFailed=function() {
    this.testFailed = true;
    this.getTestSuite().markFailed();
    LOG.error("Abort test execution due errors");
	if (this.currentTest) {
		LOG.info('Finished test case "'
				+ testFrame.getCurrentTestCase().pathname + '" at: '
				+ (new Date().getTime()));
		htmlTestRunner.runAllTests=false;
		this.currentTest.testComplete();
	}
	this.getTestSuite().getCurrentRow().saveTestResults();
    new ExtSeleniumTestResult().post();
    new SeleniumTestResult(this.getTestSuite().failed, this.getTestSuite().getTestTable()).post();
    if (this.currentTest) {
    	this.currentTest.pause();
    }
}

SeleniumTestResult.prototype.post=function () {
    if (!this.controlPanel.isAutomatedRun()) {
        return;
    }
    var form = document.createElement("form");
    document.body.appendChild(form);

    form.id = "resultsForm";
    form.method = "post";
    form.target = "selenium_myiframe";

    var resultsUrl = this.controlPanel.getResultsUrl();
    if (!resultsUrl) {
        resultsUrl = "./postResults";
    }

    var actionAndParameters = resultsUrl.split('?', 2);
    form.action = actionAndParameters[0];
    var resultsUrlQueryString = actionAndParameters[1];

    form.createHiddenField = function(name, value) {
        input = document.createElement("input");
        input.type = "hidden";
        input.name = name;
        input.value = value;
        this.appendChild(input);
    };

    if (resultsUrlQueryString) {
        var clauses = resultsUrlQueryString.split('&');
        for (var i = 0; i < clauses.length; i++) {
            var keyValuePair = clauses[i].split('=', 2);
            var key = unescape(keyValuePair[0]);
            var value = unescape(keyValuePair[1]);
            form.createHiddenField(key, value);
        }
    }

    form.createHiddenField("selenium.version", Selenium.version);
    form.createHiddenField("selenium.revision", Selenium.revision);

    form.createHiddenField("result", this.suiteFailed ? "failed" : "passed");

    form.createHiddenField("totalTime", Math.floor((this.metrics.currentTime - this.metrics.startTime) / 1000));
    form.createHiddenField("numTestPasses", this.metrics.numTestPasses);
    form.createHiddenField("numTestFailures", this.metrics.numTestFailures);
    form.createHiddenField("numCommandPasses", this.metrics.numCommandPasses);
    form.createHiddenField("numCommandFailures", this.metrics.numCommandFailures);
    form.createHiddenField("numCommandErrors", this.metrics.numCommandErrors);

    // Create an input for each test table.  The inputs are named
    // testTable.1, testTable.2, etc.
    for (rowNum = 1; rowNum < this.suiteTable.rows.length; rowNum++) {
        // If there is a second column, then add a new input
        if (this.suiteTable.rows[rowNum].cells.length > 1) {
            var resultCell = this.suiteTable.rows[rowNum].cells[1];
            form.createHiddenField("testTable." + rowNum, resultCell.innerHTML);
            // remove the resultCell, so it's not included in the suite HTML
            resultCell.parentNode.removeChild(resultCell);
        } else {
        	form.createHiddenField("testTable." + rowNum, "Not run");
        }
    }

    form.createHiddenField("numTestTotal", rowNum-1);

    // Add HTML for the suite itself
    form.createHiddenField("suite", this.suiteTable.parentNode.innerHTML);

    var logMessages = [];
    while (LOG.pendingMessages.length > 0) {
        var msg = LOG.pendingMessages.shift();
        logMessages.push(msg.type);
        logMessages.push(": ");
        logMessages.push(msg.msg);
        logMessages.push('\n');
    }
    var logOutput = logMessages.join("");
    form.createHiddenField("log", logOutput);

    if (this.controlPanel.shouldSaveResultsToFile()) {
        this._saveToFile(resultsUrl, form);
    } else {
        form.submit();
    }
    document.body.removeChild(form);
    if (this.controlPanel.closeAfterTests()) {
        window.top.close();
    }
}
