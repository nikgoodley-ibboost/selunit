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

import java.io.File;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.server.htmlrunner.HTMLLauncher;
import org.openqa.selenium.server.htmlrunner.HTMLTestResults;
import org.selunit.ServerLauncherTest;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.rc.config.RCSeleniumProperties;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestSuiteReport;

/**
 * Test for report generation in the case of fail fast setting.
 * 
 * @author mbok
 * 
 */
public class ExtHTMLTestReportBuilderFailedFastTest extends ServerLauncherTest {
	private HTMLTestResults testResults;
	private HTMLLauncher launcher;
	private static RCSeleniumProperties props = new RCSeleniumProperties();
	static {
		props.setFailFast(true);
	}

	@Before
	public void initTestResults() throws Exception {
		launcher = new HTMLLauncher(getServer());
		launcher.runHTMLSuite("*firefox", "http://www.google.de", new File(
				"src/test/resources/SuiteSimple.html"), new File(
				"target/SuiteSimple.log.html"), 30, true);
		testResults = launcher.getResults();
	}

	@Test
	public void testSuiteMetadata() throws Exception {
		RCSeleniumProperties props = new RCSeleniumProperties();
		props.setBrowserKey("*firefox");
		DefaultTestJob exec = new DefaultTestJob();
		exec.setSeleniumProperties(props);
		TestSuiteReport suite = new ExtHTMLTestReportBuilder().build(
				testResults, exec, "filename.html");
		Assert.assertTrue(exec == suite.getJobInfo());
		Assert.assertEquals(ResultType.FAILED, suite.getResultType());
		Assert.assertEquals("filename.html", suite.getFileName());
		Assert.assertEquals(suite.getTestCases().get(0).getTime(),
				suite.getTime(), 0.01);
		Assert.assertEquals("Test Suite", suite.getName());
		Assert.assertEquals(suite.getTestCases().get(0).getStartTime(),
				suite.getStartTime());
		Assert.assertEquals(suite.getTestCases().get(0).getEndTime(),
				suite.getEndTime());
	}

	@Override
	protected Capabilities getCapabilities() {
		return new DesiredCapabilities(props.getCapabilities());
	}

	@Test
	public void testCaseMetadataFailedFast() throws Exception {
		RCSeleniumProperties props = new RCSeleniumProperties();
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
		Assert.assertEquals(0, tc.getStartTime());
		Assert.assertEquals(0, tc.getEndTime());
		Assert.assertEquals("testcases/GoogleSearch2.html", tc.getFileName());
		Assert.assertEquals(ResultType.NOT_RUN, tc.getResultType());
		Assert.assertEquals("GoogleSearch2", tc.getName());
		Assert.assertTrue(tc.getResultLog().getHtmlSummary().indexOf("Not run") > 0);

		// TestCase: GoogleSearch3
		tc = suite.getTestCases().get(2);
		Assert.assertEquals(0, tc.getStartTime());
		Assert.assertEquals(0, tc.getEndTime());
		Assert.assertEquals("testcases/GoogleSearch3.html", tc.getFileName());
		Assert.assertEquals(ResultType.NOT_RUN, tc.getResultType());
		Assert.assertEquals("GoogleSearch3", tc.getName());
		Assert.assertTrue(tc.getResultLog().getHtmlSummary().indexOf("Not run") > 0);
	}
}
