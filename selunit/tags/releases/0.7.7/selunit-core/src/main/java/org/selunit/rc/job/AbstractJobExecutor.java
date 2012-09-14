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
package org.selunit.rc.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.server.htmlrunner.HTMLLauncher;
import org.selunit.config.SeleniumProperties;
import org.selunit.config.support.ExtRemoteControlConfiguration;
import org.selunit.job.JobExecutor;
import org.selunit.job.JobExecutorHandler;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.job.support.DefaultExecutionStatus;
import org.selunit.rc.config.RCSeleniumProperties;
import org.selunit.rc.report.builder.BuilderException;
import org.selunit.rc.report.builder.ExtHTMLTestReportBuilder;
import org.selunit.rc.report.server.ExtSeleniumServer;
import org.selunit.rc.testpackage.TestResourceLocator;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.output.OutputProcessor;
import org.selunit.report.support.DefaultTestSuite;
import org.selunit.testpackage.TestResource;
import org.selunit.testpackage.TestResourceAccess;

public abstract class AbstractJobExecutor<J extends TestJob> implements
		JobExecutor<J> {

	private Log log = LogFactory.getLog(getClass());

	private List<JobExecutorHandler<J>> handlers = new ArrayList<JobExecutorHandler<J>>();

	private ExtHTMLTestReportBuilder reportBuilder = new ExtHTMLTestReportBuilder();

	private DefaultExecutionStatus status;

	private OutputProcessor<TestSuiteReport> reportOutputProcessor;

	private TestResourceAccess testResourceAccess;

	private String getJobServerContext(TestJob job) {
		return "/testjob-" + job.getId();
	}

	protected ExtSeleniumServer createServerInstance(TestJob job)
			throws TestJobException {
		ExtSeleniumServer server;
		try {
			RCSeleniumProperties props = new RCSeleniumProperties(
					job.getSeleniumProperties());
			server = new ExtSeleniumServer(false, getServerConfiguration(props));
			TestResourceLocator testResourceLocator = new TestResourceLocator(
					job, getTestResourceAccess(), getJobServerContext(job));
			testResourceLocator.setProvideUserExtensions(props
					.getUserExtensions() != null);
			server.addStaticResourceLocator(testResourceLocator);
			return server;
		} catch (Exception e) {
			throw new TestJobException(job,
					"Failed to initialize selenium server", e);
		}
	}

	/**
	 * Returns selenium server configuration transformed from
	 * {@link SeleniumProperties}.
	 * 
	 * @param props
	 *            test runtime properties
	 * @return selenium server configuration transformed from
	 *         {@link SeleniumProperties}
	 */
	protected ExtRemoteControlConfiguration getServerConfiguration(
			RCSeleniumProperties props) {
		ExtRemoteControlConfiguration conf = new ExtRemoteControlConfiguration();
		conf.setPort(props.getPort());
		conf.setSingleWindow(!props.isMultiWindow());
		if (props.getCapabilities() != null) {
			conf.setBrowserCapabilities(new DesiredCapabilities(props
					.getCapabilities()));
		}
		// user-extensions.js is handled by #createServerInstance
		return conf;
	}

	/**
	 * Launches given suite against the server and return built test reports.
	 * 
	 * @param server
	 *            to run suite against
	 * @param job
	 *            the suite is part of
	 * @param suiteResource
	 *            the suite resource
	 * @return Built test report for launched suite
	 * @throws TestJobException
	 *             in case of errors
	 */
	protected DefaultTestSuite launchSuite(ExtSeleniumServer server,
			TestJob job, TestResource suiteResource) throws TestJobException {
		HTMLLauncher launcher = new HTMLLauncher(server);
		RCSeleniumProperties selProps = new RCSeleniumProperties(
				job.getSeleniumProperties());
		try {
			String suiteLocalPath = suiteResource.getName();
			if (suiteLocalPath.startsWith("/")) {
				suiteLocalPath = suiteLocalPath.replaceAll("^/+", "");
			}
			String suiteURL = (new StringBuilder())
					.append("http://localhost:")
					.append(server.getConfiguration()
							.getPortDriversShouldContact())
					.append("/selenium-server" + getJobServerContext(job) + "/")
					.append(suiteLocalPath).toString();
			log.debug("Launching test suite for job (" + job + "): " + suiteURL);
			launcher.runHTMLSuite(selProps.getBrowserKey(),
					selProps.getBrowserURL(), suiteURL,
					File.createTempFile("selenium", "html"),
					selProps.getTimeoutInSeconds(), selProps.isMultiWindow());
			log.debug("Building report for suite \"" + suiteResource
					+ "\" for job: " + job);
			return reportBuilder.build(launcher.getResults(), job,
					suiteResource.getName());
		} catch (BuilderException e) {
			throw new TestJobException(job,
					"Failed to build report for suite: " + suiteResource, e);
		} catch (Throwable e) {
			throw new TestJobException(job, "Failed to launch suite: "
					+ suiteResource, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mbok.selenium.job.TestJobExecutor#addHandler(de.mbok.selenium.job.
	 * ExecutionHandler)
	 */
	@Override
	public void addHandler(JobExecutorHandler<J> handler) {
		handlers.add(handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mbok.selenium.job.TestJobExecutor#getHandlers()
	 */
	@Override
	public List<JobExecutorHandler<J>> getHandlers() {
		return Collections.unmodifiableList(handlers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mbok.selenium.job.TestJobExecutor#removeHandler(de.mbok.selenium.job
	 * .ExecutionHandler)
	 */
	@Override
	public void removeHandler(JobExecutorHandler<J> handler) {
		handlers.remove(handler);
	}

	/**
	 * @return the status
	 */
	@Override
	public DefaultExecutionStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	protected void setStatus(DefaultExecutionStatus status) {
		this.status = status;
	}

	/**
	 * @return the reportOutputProcessor
	 */
	public OutputProcessor<TestSuiteReport> getReportOutputProcessor() {
		return reportOutputProcessor;
	}

	/**
	 * @param reportOutputProcessor
	 *            the reportOutputProcessor to set
	 */
	public void setReportOutputProcessor(
			OutputProcessor<TestSuiteReport> reportOutputProcessor) {
		this.reportOutputProcessor = reportOutputProcessor;
	}

	/**
	 * @return the testResourceAccess
	 */
	public TestResourceAccess getTestResourceAccess() {
		return testResourceAccess;
	}

	/**
	 * @param testResourceAccess
	 *            the testResourceAccess to set
	 */
	public void setTestResourceAccess(TestResourceAccess testResourceAccess) {
		this.testResourceAccess = testResourceAccess;
	}

	/**
	 * @param handlers
	 *            the handlers to set
	 */
	public void setHandlers(List<JobExecutorHandler<J>> handlers) {
		this.handlers = handlers;
	}

}
