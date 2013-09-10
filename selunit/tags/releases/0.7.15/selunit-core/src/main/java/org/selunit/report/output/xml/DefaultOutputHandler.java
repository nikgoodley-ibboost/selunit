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
package org.selunit.report.output.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.selunit.job.TestJobInfo;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestReportLog;
import org.selunit.report.TestSuiteReport;
import org.w3c.tidy.Tidy;


public class DefaultOutputHandler implements XMLOutputHandler<TestSuiteReport> {
	private static Log log = LogFactory.getLog(DefaultOutputHandler.class);
	public static final String NODE_JOB_INFO = "job-info";
	public static final String NODE_PROP = "prop";
	public static final String NODE_PROP_ATT_NAME = "name";
	public static final String NODE_PROP_ATT_VALUE = "value";

	@Override
	public void handleOutput(TestSuiteReport suite, Document jdom)
			throws IOException {
		log.info("Generate report DOM for " + suite);
		jdom.setDocType(new DocType("testsuite",
				"-//Selunit//DTD Selunit Report 1.0.0//EN",
				"selunit-report_1_0_0.dtd"));
		try {
			jdom.setRootElement(getSuiteElement(suite));
		} catch (JDOMException e) {
			throw new IOException("Error during output of suite: " + suite, e);
		}
	}

	private Element getSuiteElement(TestSuiteReport suite)
			throws JDOMException, IOException {
		Element suiteElement = new Element("testsuite");
		suiteElement.setAttribute("name", suite.getName());
		suiteElement.setAttribute("time", Double.toString(suite.getTime()));
		suiteElement.setAttribute("start-time",
				Long.toString(suite.getStartTime()));
		suiteElement
				.setAttribute("end-time", Long.toString(suite.getEndTime()));
		suiteElement.setAttribute("total", suite.getTestCases().size() + "");
		suiteElement.setAttribute("iteration", suite.getIterationCount() + "");

		suiteElement.addContent(getJobInfoElement(suite.getJobInfo()));

		int passes = 0, failures = 0;
		for (TestCaseReport tc : suite.getTestCases()) {
			suiteElement.addContent(getTestCaseElement(tc));
			if (tc.getResultType() == ResultType.PASSED) {
				passes++;
			} else if (tc.getResultType() == ResultType.FAILED) {
				failures++;
			}
		}
		suiteElement.setAttribute("passes", passes + "");
		suiteElement.setAttribute("failures", failures + "");

		return suiteElement;
	}

	private Element getJobInfoElement(TestJobInfo jobInfo) {
		Element jobEl = new Element(NODE_JOB_INFO);
		if (jobInfo.getId() != null) {
			jobEl.setAttribute("id", jobInfo.getId());
		}
		if (jobInfo.getName() != null) {
			jobEl.setAttribute("name", jobInfo.getName());
		}
		if (jobInfo.getEnvironment().getOs() != null) {
			jobEl.setAttribute("os", jobInfo.getEnvironment().getOs());
		}
		if (jobInfo.getEnvironment().getBrowser() != null) {
			jobEl.setAttribute("browser", jobInfo.getEnvironment().getBrowser());
		}

		if (jobInfo.getProject() != null) {
			Element projectEl = new Element("project");
			if (jobInfo.getProject().getId() != null) {
				projectEl.setAttribute("id", jobInfo.getProject().getId());
			}
			if (jobInfo.getProject().getName() != null) {
				projectEl.setAttribute("name", jobInfo.getProject().getName());
			}
			jobEl.addContent(projectEl);
		}

		Element seleniumEl = new Element("selenium");
		jobEl.addContent(seleniumEl);
		Map<String,?> selProps = jobInfo.getSeleniumProperties().getCapabilities();
		for (String name : selProps.keySet()) {
			Element propEl = new Element(NODE_PROP);
			propEl.setAttribute(NODE_PROP_ATT_NAME, name);
			propEl.setAttribute(NODE_PROP_ATT_VALUE, selProps.get(name).toString());
			seleniumEl.addContent(propEl);
		}
		return jobEl;
	}

	private Element getTestCaseElement(TestCaseReport testCase)
			throws JDOMException, IOException {
		Element ec = new Element("testcase");
		ec.setAttribute("name", testCase.getName());
		ec.setAttribute("file", testCase.getFileName());
		ec.setAttribute("result", testCase.getResultType().toString());
		ec.setAttribute("time", Double.toString(testCase.getTime()));
		ec.setAttribute("start-time", Long.toString(testCase.getStartTime()));
		ec.setAttribute("end-time", Long.toString(testCase.getEndTime()));
		if (testCase.getResultLog() != null) {
			ec.addContent(getLogElement(testCase.getResultLog()));
		}
		return ec;
	}

	private Element getLogElement(TestReportLog log) throws JDOMException,
			IOException {
		Element el = new Element("log");
		el.addContent(getHtmlSummaryElement(log.getHtmlSummary()));
		Element systemLogEl = new Element("system-log");
		systemLogEl.addContent(new CDATA(log.getSystemLog()));
		el.addContent(systemLogEl);
		return el;
	}

	private Element getHtmlSummaryElement(String html) throws JDOMException,
			IOException {
		Tidy tidy = new Tidy();
		tidy.setForceOutput(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setXHTML(true);
		tidy.setPrintBodyOnly(true);
		StringWriter tidiedHtml = new StringWriter();
		tidy.parse(new StringReader(html), tidiedHtml);
		Element htmlSummaryEl = new Element("html-summary");
		htmlSummaryEl.addContent(new CDATA(tidiedHtml.toString()));
		return htmlSummaryEl;
	}
}
