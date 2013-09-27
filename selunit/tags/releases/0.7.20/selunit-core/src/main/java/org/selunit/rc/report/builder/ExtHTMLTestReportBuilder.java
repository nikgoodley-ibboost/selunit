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
package org.selunit.rc.report.builder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.server.htmlrunner.HTMLTestResults;
import org.selunit.job.TestJobInfo;
import org.selunit.rc.report.builder.CaseSystemLogExtractor.CaseLogPair;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.support.DefaultResultLog;
import org.selunit.report.support.DefaultTestCase;
import org.selunit.report.support.DefaultTestSuite;

/**
 * Generates report API object from raw selenium logs.
 * 
 * @author mbok
 * 
 */
public class ExtHTMLTestReportBuilder {
	private static Log log = LogFactory.getLog(ExtHTMLTestReportBuilder.class);
	private static CaseSystemLogExtractor logExtractor = new CaseSystemLogExtractor();

	public static final String CSS_CLASS_STATUS_NOT_RUN = "status_not_run";

	public DefaultTestSuite build(HTMLTestResults testResults,
			TestJobInfo jobInfo, String fileName) throws BuilderException {
		DefaultTestSuite suite = new DefaultTestSuite();
		suite.setFileName(fileName);
		suite.setJobInfo(jobInfo);
		StringWriter htmlResult = new StringWriter();
		try {
			testResults.write(htmlResult);
			Document htmlResultDoc = Jsoup.parse(htmlResult.toString());
			buildSuiteMetadata(suite, testResults, htmlResultDoc);
			suite.setTestCases(buildTestCases(suite, testResults, htmlResultDoc));
			if (suite.getTestCases().size() > 0) {
				int ci = 0;
				for (TestCaseReport c : suite.getTestCases()) {
					if (StringUtils.isEmpty(c.getName())) {
						((DefaultTestCase) c).setName(getElementText(
								htmlResultDoc
										.select("body > table:eq(1) table a"),
								1 + ci++, c.getFileName()));
					}
					if (c.getStartTime() > 0 && suite.getStartTime() <= 0) {
						suite.setStartTime(c.getStartTime());
					}
					if (c.getStartTime() < c.getEndTime()) {
						suite.setEndTime(c.getEndTime());
					}
				}
				suite.setTime(((double) suite.getEndTime() - suite
						.getStartTime()) / 1000);
			}
		} catch (Exception e) {
			throw new BuilderException("Failed to process html results", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Built suite: " + suite);
		}
		return suite;
	}

	private String getElementText(Elements elements, int childNum,
			String failbackText) {
		if (childNum < elements.size()) {
			return elements.get(childNum).text();
		} else {
			return failbackText;
		}
	}

	private void buildSuiteMetadata(DefaultTestSuite suite,
			HTMLTestResults testResults, Document htmlResultDoc) {
		suite.setTime(Double.parseDouble(testResults.getTotalTime()));
		String suiteName = getElementText(
				htmlResultDoc.select("body > table:eq(1) tr.title td"), 0,
				suite.getFileName());
		log.info("Identified suite name: " + suiteName);
		suite.setName(suiteName);
	}

	private List<TestCaseReport> buildTestCases(DefaultTestSuite suite,
			HTMLTestResults testResults, Document htmlResultDoc) {
		ArrayList<TestCaseReport> cases = new ArrayList<TestCaseReport>();
		ResultType suiteResultType = ResultType.PASSED;

		String systemLog = htmlResultDoc.select("pre").text().trim();
		List<CaseLogPair> caseLogs = logExtractor.extractCaseLogs(systemLog);

		Elements caseRows = htmlResultDoc
				.select("body > table:eq(2) > tbody > tr");

		for (Element ci : caseRows) {
			DefaultTestCase co = new DefaultTestCase();
			co.setFileName(ci.select("a:eq(0)").text());
			Elements titleRow = ci.select("tr.title:eq(0)");
			co.setName(titleRow.text());
			co.setResultType(ResultType.NOT_RUN);
			if (titleRow.hasClass("status_failed")) {
				co.setResultType(ResultType.FAILED);
				suiteResultType = ResultType.FAILED;
			} else if (titleRow.hasClass("status_passed")) {
				co.setResultType(ResultType.PASSED);
			} else {
				titleRow.addClass(CSS_CLASS_STATUS_NOT_RUN);
			}

			DefaultResultLog cLog = new DefaultResultLog("", "");
			if (co.getResultType() != ResultType.NOT_RUN) {
				ci.select("table").removeAttr("border")
						.removeAttr("cellpadding").removeAttr("cellspacing");
				cLog.setHtmlSummary(ci.select("tr>td>div").html());
				// Case logs and timestamps
				if (caseLogs.size() > 0) {
					CaseLogPair logPair = caseLogs.remove(0);
					if (logPair.getCaseFileName().endsWith(co.getFileName())) {
						cLog.setSystemLog(logPair.getLog());
						co.setTime(((double) logPair.getEndTime() - logPair
								.getStartTime()) / 1000);
						co.setStartTime(logPair.getStartTime());
						co.setEndTime(logPair.getEndTime());
					} else {
						log.error("File name from case log ("
								+ logPair.getCaseFileName()
								+ ") doesn't match the case file name ("
								+ co.getFileName()
								+ "). System logs and timestamps won't be provided for: "
								+ suite);
					}
				} else {
					log.error("Amount of extracted case logs doesn't match amount of test cases. System logs and timestamps won't be provided for: "
							+ suite);
				}
			}
			co.setResultLog(cLog);
			if (log.isDebugEnabled()) {
				log.debug("Built test case: " + co);
			}
			cases.add(co);
		}
		suite.setResultType(suiteResultType);
		return cases;

	}
}
