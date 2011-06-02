package org.selunit.report.output.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selunit.config.support.DefaultEnvironmentInfo;
import org.selunit.config.support.DefaultSeleniumProperties;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.report.ResultType;
import org.selunit.report.TestCaseReport;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.output.OutputStreamFactory;
import org.selunit.report.output.xml.ReportEntityResolver;
import org.selunit.report.output.xml.XMLProcessor;
import org.selunit.report.support.DefaultResultLog;
import org.selunit.report.support.DefaultTestCase;
import org.selunit.report.support.DefaultTestSuite;
import org.selunit.support.DefaultTestProject;


public class XMLProcessorTest {
	private DefaultTestSuite suite;

	@SuppressWarnings("unchecked")
	@Before
	public void initSuite() {
		suite = new DefaultTestSuite();
		suite.setName("SuiteTestMe");
		suite.setTime(0.5678);
		suite.setTestCases(Collections.EMPTY_LIST);

		DefaultSeleniumProperties props = new DefaultSeleniumProperties();
		props.setBrowserKey("*firefox");
		DefaultTestJob job = new DefaultTestJob();
		job.setName("TT1");
		job.setId("123456789");
		job.setEnvironment(new DefaultEnvironmentInfo("Windows 2003",
				"Firefox 3.5"));

		job.setSeleniumProperties(props);
		DefaultTestProject project = new DefaultTestProject();
		project.setName("Product A v.1");
		project.setId("987654321");
		job.setProject(project);
		suite.setJobInfo(job);

		DefaultTestCase case1 = new DefaultTestCase();
		case1.setName("case1");
		case1.setFileName("case1.html");
		case1.setResultType(ResultType.PASSED);
		case1.setTime(0.1);
		case1.setResultLog(new DefaultResultLog("<b>OK</b>", "INFO ..."));
		case1.setStartTime(1);
		case1.setEndTime(2);

		DefaultTestCase case2 = new DefaultTestCase();

		case2.setName("case2");
		case2.setFileName("case2.html");
		case2.setResultType(ResultType.FAILED);
		case2.setTime(0.3);
		case2.setResultLog(new DefaultResultLog("<b>NOK</b>", "WARN ..."));
		case2.setStartTime(3);
		case2.setEndTime(4);

		suite.setTestCases(new ArrayList<TestCaseReport>());
		suite.getTestCases().add(case1);
		suite.getTestCases().add(case2);
		suite.setStartTime(1);
		suite.setEndTime(4);
	}

	@Test
	public void testXMLOutputAgainstDTD() throws Exception {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLProcessor gen = new XMLProcessor();
		gen.setOutputStreamFactory(new OutputStreamFactory<TestSuiteReport>() {
			@Override
			public OutputStream createReportOutputStream(TestSuiteReport suite)
					throws IOException {
				return output;
			}
		});
		gen.processOutput(suite);
		String strOutput = new String(output.toByteArray(), "UTF-8");
		System.out.println(strOutput);
		SAXBuilder builder = new SAXBuilder(true);
		builder.setEntityResolver(new ReportEntityResolver());
		builder.build(new StringReader(strOutput));
	}
}
