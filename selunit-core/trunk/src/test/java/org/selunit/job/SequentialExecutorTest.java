package org.selunit.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.selunit.config.support.DefaultEnvironmentInfo;
import org.selunit.config.support.DefaultSeleniumProperties;
import org.selunit.job.JobExecutorHandler;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.job.JobStatus.StatusType;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.job.support.JobHandlerAdapter;
import org.selunit.job.support.SequentialExecutor;
import org.selunit.report.ResultType;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.output.OutputProcessException;
import org.selunit.report.output.OutputProcessor;
import org.selunit.support.DefaultTestProject;
import org.selunit.testpackage.TestResourceAccess;
import org.selunit.testpackage.file.DirectoryFileAccess;


public class SequentialExecutorTest {
	private DefaultTestJob job;
	private ArrayList<String> suites = new ArrayList<String>();
	private TestResourceAccess resourceAccess;

	@Before
	public void initJob() throws Exception {
		DefaultTestProject project = new DefaultTestProject();
		project.setName("Product A v.1");
		project.setId("project-" + System.currentTimeMillis());
		resourceAccess = new DirectoryFileAccess(new File("src/test/resources"));

		suites.add("SuiteSimple.html");
		suites.add("SuiteSimpleCopy.html");
		DefaultSeleniumProperties props = new DefaultSeleniumProperties();
		props.setBrowserKey("*firefox");
		props.setBrowserURL("http://www.google.de");
		job = new DefaultTestJob();
		job.setProject(project);
		job.setSeleniumProperties(props);
		job.setId("job-" + System.currentTimeMillis());
		job.setName("Test track 1");
		job.setEnvironment(new DefaultEnvironmentInfo("Windows 2003",
				"Firefox 3.5"));
		job.setSuites(suites);
	}

	// @Test
	public void stestSequentialExecutor() throws Exception {
		final ArrayList<TestSuiteReport> reports = new ArrayList<TestSuiteReport>();
		SequentialExecutor<DefaultTestJob> exec = new SequentialExecutor<DefaultTestJob>();
		exec.setTestResourceAccess(resourceAccess);
		exec.setReportOutputProcessor(new OutputProcessor<TestSuiteReport>() {

			@Override
			public void processOutput(TestSuiteReport suite)
					throws OutputProcessException {
				reports.add(suite);
				Assert.assertNotNull(suite);
			}
		});
		// Before start
		Assert.assertEquals(0, reports.size());
		exec.init(job);
		Assert.assertEquals(StatusType.INITIALIZED, exec.getStatus().getType());
		exec.start(suites);
		Assert.assertEquals(StatusType.EXECUTING_SUITES, exec.getStatus()
				.getType());
		while (exec.getStatus().getType() != StatusType.FINISHED_SUITES) {
			Thread.sleep(1000);
		}
		// After first execution
		Assert.assertNull(exec.getStatus().getExecutionError());
		Assert.assertEquals(2, reports.size());

		// Continue same execution with only the first suite
		exec.start(suites.subList(0, 1));
		Assert.assertEquals(StatusType.EXECUTING_SUITES, exec.getStatus()
				.getType());
		while (exec.getStatus().getType() != StatusType.FINISHED_SUITES) {
			Thread.sleep(1000);
		}
		// After execution
		Assert.assertNull(exec.getStatus().getExecutionError());
		Assert.assertEquals(3, reports.size());

		exec.stop(false);
		Assert.assertEquals(StatusType.STOPPED, exec.getStatus().getType());
	}

	// @Test
	public void stestExecutionStopping() throws Exception {
		final ArrayList<TestSuiteReport> reports = new ArrayList<TestSuiteReport>();
		final SequentialExecutor<DefaultTestJob> exec = new SequentialExecutor<DefaultTestJob>();
		exec.setTestResourceAccess(resourceAccess);
		exec.setReportOutputProcessor(new OutputProcessor<TestSuiteReport>() {
			@Override
			public void processOutput(TestSuiteReport suite)
					throws OutputProcessException {
				reports.add(suite);
				Assert.assertNotNull(suite);
				try {
					exec.stop(false);
					Assert.assertEquals(StatusType.STOPPING, exec.getStatus()
							.getType());
				} catch (TestJobException e) {
					Assert.fail();
				}
			}
		});
		// Before start
		exec.init(job);
		exec.start(suites);
		long startWhile = System.currentTimeMillis();
		while (exec.getStatus().getType() != StatusType.STOPPED) {
			Thread.sleep(1000);
			if (System.currentTimeMillis() - startWhile > 1000 * 60) {
				Assert.fail("Unexpected timeout");
			}
		}
		// After execution/stop, only the first suite should be launched.
		Assert.assertNull("Error should be null: "
				+ exec.getStatus().getExecutionError(), exec.getStatus()
				.getExecutionError());
		Assert.assertEquals(1, reports.size());
		Assert.assertEquals("Test Suite", reports.get(0).getName());
	}

	// @Test
	public void stestShutdown() throws Exception {
		final SequentialExecutor<DefaultTestJob> exec = new SequentialExecutor<DefaultTestJob>();
		exec.setTestResourceAccess(resourceAccess);
		ArrayList<JobExecutorHandler<DefaultTestJob>> handlers = new ArrayList<JobExecutorHandler<DefaultTestJob>>();
		final ArrayList<Boolean> startedSuite = new ArrayList<Boolean>();
		final ArrayList<TestJob> initedJob = new ArrayList<TestJob>();
		handlers.add(new JobHandlerAdapter<DefaultTestJob>() {

			@Override
			public void startTestSuite(DefaultTestJob job, String suite)
					throws TestJobException {
				startedSuite.add(true);
			}

			@Override
			public void initJob(DefaultTestJob job) throws TestJobException {
				initedJob.add(job);
			}

			@Override
			public void stopJob(DefaultTestJob job) throws TestJobException {
				initedJob.remove(job);
			}
		});
		exec.setHandlers(handlers);
		Assert.assertEquals(0, initedJob.size());
		exec.init(job);
		Assert.assertEquals(job, initedJob.get(0));
		exec.start(Collections.singletonList("SuiteLongDummyWait.html"));
		long startWhile = System.currentTimeMillis();
		while (exec.getStatus().getType() != StatusType.STOPPED) {
			boolean startShutdown = startedSuite.size() > 0;
			Thread.sleep(10000);
			if (startShutdown) {
				startedSuite.clear();
				System.out.println("Terminating executor...");
				exec.stop(true);
			}
			if (System.currentTimeMillis() - startWhile > 1000 * 60) {
				Assert.fail("Unexpected timeout");
			}
		}
		Assert.assertEquals(0, initedJob.size());
	}

	@Test
	public void testUserExtensions() throws Exception {
		final SequentialExecutor<DefaultTestJob> exec = new SequentialExecutor<DefaultTestJob>();
		exec.setTestResourceAccess(resourceAccess);
		final ArrayList<TestSuiteReport> reports = new ArrayList<TestSuiteReport>();
		exec.setReportOutputProcessor(new OutputProcessor<TestSuiteReport>() {
			@Override
			public void processOutput(TestSuiteReport suite)
					throws OutputProcessException {
				reports.add(suite);
				Assert.assertNotNull(suite);
			}
		});
		((DefaultSeleniumProperties) job.getSeleniumProperties())
				.setUserExtensions("user-ext.js");
		exec.init(job);
		exec.start(Collections.singletonList("SuiteUserExt.html"));
		long startWhile = System.currentTimeMillis();
		while (exec.getStatus().getType() == StatusType.EXECUTING_SUITES) {
			Thread.sleep(200);
			if (System.currentTimeMillis() - startWhile > 1000 * 60) {
				Assert.fail("Unexpected timeout");
			}
		}
		exec.stop(true);
		Assert.assertEquals(ResultType.PASSED, reports.get(0).getResultType());
		Assert.assertTrue(reports.get(0).getTestCases().get(0).getResultLog()
				.getSystemLog().contains("User extensions works!"));
	}
}
