package org.selunit.report.builder;

import java.io.File;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.server.htmlrunner.HTMLTestResults;
import org.selunit.ServerLauncherTest;
import org.selunit.config.support.DefaultSeleniumProperties;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.builder.ExtHTMLTestReportBuilder;
import org.selunit.report.server.ExtHTMLLauncher;


public class ExtHTMLTestReportBuilderTest extends ServerLauncherTest {
	private HTMLTestResults testResults;
	private ExtHTMLLauncher launcher;

	@Before
	public void initTestResults() throws Exception {
		launcher = new ExtHTMLLauncher(getServer());
		launcher.runHTMLSuite("*firefox", "http://www.google.de", new File(
				"src/test/resources/SuiteSimple.html"), new File(
				"target/SuiteSimple.log.html"), 30, true);
		testResults = launcher.getResults();
	}

	@Test
	public void testSuiteMetadata() throws Exception {
		DefaultSeleniumProperties props = new DefaultSeleniumProperties();
		props.setBrowserKey("*firefox");
		DefaultTestJob exec = new DefaultTestJob();
		exec.setSeleniumProperties(props);
		TestSuiteReport suite = new ExtHTMLTestReportBuilder().build(
				testResults, exec, "filename.html");
		Assert.assertTrue(exec == suite.getJobInfo());
		Assert.assertEquals("filename.html", suite.getFileName());
		Assert.assertEquals(
				((double) suite.getTestCases().get(2).getEndTime() - suite
						.getTestCases().get(0).getStartTime()) / 1000,
				suite.getTime(), 0.01);
		Assert.assertEquals("Test Suite", suite.getName());
		Assert.assertEquals(suite.getTestCases().get(0).getStartTime(),
				suite.getStartTime());
		Assert.assertEquals(
				suite.getTestCases().get(suite.getTestCases().size() - 1)
						.getEndTime(), suite.getEndTime());
	}

	@Test
	public void testCaseMetadata() throws Exception {
		DefaultSeleniumProperties props = new DefaultSeleniumProperties();
		props.setBrowserKey("*firefox");
		DefaultTestJob exec = new DefaultTestJob();
		exec.setSeleniumProperties(props);
		TestSuiteReport suite = new ExtHTMLTestReportBuilder().build(
				testResults, exec, "filename.html");
		Assert.assertEquals(3, suite.getTestCases().size());

		// TestCase: GoogleSearch1
		TestCaseReport tc = suite.getTestCases().get(0);
		Assert.assertEquals("GoogleSearch1", tc.getName());
		Assert.assertEquals("testcases/GoogleSearch1.html", tc.getFileName());
		Assert.assertEquals(ResultType.FAILED, tc.getResultType());
		String errorMsg = Jsoup.parse(tc.getResultLog().getHtmlSummary())
				.select("table>tbody>tr>td").get(8).text().trim()
				.replace('\u00A0', ' ');
		Assert.assertEquals("Element qnull not found", errorMsg);
		Assert.assertTrue(tc.getStartTime() < tc.getEndTime());

		// TestCase: GoogleSearch2
		tc = suite.getTestCases().get(1);
		Assert.assertTrue(suite.getTestCases().get(0).getEndTime() <= tc
				.getStartTime());
		Assert.assertTrue(tc.getStartTime() < tc.getEndTime());
		Assert.assertEquals("GoogleSearch2", tc.getName());
		Assert.assertEquals("testcases/GoogleSearch2.html", tc.getFileName());
		Assert.assertEquals(ResultType.FAILED, tc.getResultType());
		errorMsg = Jsoup.parse(tc.getResultLog().getHtmlSummary())
				.select("table>tbody>tr>td").get(8).text().trim()
				.replace('\u00A0', ' ');
		Assert.assertEquals("Actual value '' did not match 'selenium'",
				errorMsg);

		// TestCase: GoogleSearch3
		tc = suite.getTestCases().get(2);
		Assert.assertTrue(suite.getTestCases().get(1).getEndTime() <= tc
				.getStartTime());
		Assert.assertTrue(tc.getStartTime() < tc.getEndTime());
		Assert.assertEquals("GoogleSearch3", tc.getName());
		Assert.assertEquals("testcases/GoogleSearch3.html", tc.getFileName());
		Assert.assertEquals(ResultType.PASSED, tc.getResultType());
		Assert.assertEquals(
				"selenium",
				Jsoup.parse(tc.getResultLog().getHtmlSummary())
						.select("table>tbody>tr>td").get(5).text().trim());
	}
}
