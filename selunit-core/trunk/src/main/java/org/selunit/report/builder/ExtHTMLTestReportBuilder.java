package org.selunit.report.builder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.server.htmlrunner.HTMLTestResults;
import org.selunit.job.TestJobInfo;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestReportLog;
import org.selunit.report.builder.CaseSystemLogExtractor.CaseLogPair;
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
	/**
	 * CSS class name of DIV container for each Selenium table report in
	 * {@link TestReportLog#getHtmlSummary()}.
	 */
	public static final String CSS_CLASS_SEL_REPORT = "selenium-report";

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
				suite.setStartTime(suite.getTestCases().get(0).getStartTime());
				suite.setEndTime(suite.getTestCases()
						.get(suite.getTestCases().size() - 1).getEndTime());
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

	private void buildSuiteMetadata(DefaultTestSuite suite,
			HTMLTestResults testResults, Document htmlResultDoc) {
		suite.setTime(Double.parseDouble(testResults.getTotalTime()));
		String suiteName = htmlResultDoc.select("table#suiteTable tr.title td")
				.get(0).text();
		log.info("Identified suite name: " + suiteName);
		suite.setName(suiteName);
	}

	private List<TestCaseReport> buildTestCases(DefaultTestSuite suite,
			HTMLTestResults testResults, Document htmlResultDoc) {
		ArrayList<TestCaseReport> cases = new ArrayList<TestCaseReport>();
		ResultType suiteResultType = ResultType.PASSED;

		String systemLog = htmlResultDoc.select("pre").text().trim();
		List<CaseLogPair> caseLogs = logExtractor.extractCaseLogs(systemLog);

		Elements caseRows = htmlResultDoc.select("body > table").get(1)
				.select("table > tr");

		boolean considerSystemCaseLogs = true;
		if (caseLogs.size() != caseRows.size()) {
			considerSystemCaseLogs = false;
			log.error("Amount of extracted case logs doesn't match amount of test cases. Inclusion of system logs and timestamps to reports will be deactivated!");
		}

		for (Element ci : caseRows) {
			DefaultTestCase co = new DefaultTestCase();
			co.setFileName(ci.select("a").get(0).text());
			Elements titleRow = ci.select("tr.title");
			co.setName(titleRow.first().text());
			if (titleRow.hasClass("status_failed")) {
				co.setResultType(ResultType.FAILED);
				suiteResultType = ResultType.FAILED;
			} else {
				co.setResultType(ResultType.PASSED);
			}

			// Case logs and timestamps
			DefaultResultLog cLog = new DefaultResultLog("", "");
			cLog.setHtmlSummary(ci.select("div").addClass(CSS_CLASS_SEL_REPORT)
					.outerHtml());
			if (considerSystemCaseLogs) {
				CaseLogPair logPair = caseLogs.remove(0);
				if (logPair.getCaseFileName().endsWith(co.getFileName())) {
					cLog.setSystemLog(logPair.getLog());
					co.setTime(((double) logPair.getEndTime() - logPair
							.getStartTime()) / 1000);
					co.setStartTime(logPair.getStartTime());
					co.setEndTime(logPair.getEndTime());
				} else {
					considerSystemCaseLogs = false;
					log.error("File name from case log ("
							+ logPair.getCaseFileName()
							+ ") doesn't match the case file name ("
							+ co.getFileName()
							+ "). Hence inclusion of system logs and timestamps to reports will be deactivated!");
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
