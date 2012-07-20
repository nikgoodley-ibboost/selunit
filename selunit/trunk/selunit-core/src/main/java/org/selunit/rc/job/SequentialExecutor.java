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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.selunit.job.JobExecutorHandler;
import org.selunit.job.JobStatus.StatusType;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.job.support.DefaultExecutionStatus;
import org.selunit.rc.report.server.ExtSeleniumServer;
import org.selunit.report.output.OutputProcessException;
import org.selunit.report.support.DefaultTestSuite;
import org.selunit.testpackage.TestResource;

public class SequentialExecutor<J extends TestJob> extends
		AbstractJobExecutor<J> {
	private Log log = LogFactory.getLog(getClass());

	private J job;
	private List<String> suites = new ArrayList<String>();
	private ExtSeleniumServer server;
	private SequentialExecutorThread thread;

	public SequentialExecutor() {
		setStatus(new DefaultExecutionStatus());
	}

	private class SequentialExecutorThread extends Thread {
		private boolean finsihed = true;

		@Override
		public void run() {
			try {
				finsihed = false;
				try {
					for (JobExecutorHandler<J> h : getHandlers()) {
						h.startExecution(job, suites);
					}
					int cycle = 0;
					OUTER_CYCLE: do {
						cycle++;
						log.debug("Start execution cycle " + cycle
								+ " for job: " + job);
						for (String suitePath : suites) {
							suitePath = FilenameUtils
									.separatorsToUnix(suitePath);
							TestResource suite = null;
							try {
								suite = getTestResourceAccess().getResource(
										job, suitePath);
							} catch (IOException e) {
								throw new TestJobException(job,
										"Error reading suite resource for name: "
												+ suitePath, e);
							}
							if (suite == null) {
								throw new TestJobException(job,
										"Suite resource '" + suitePath
												+ "' not found in test package");
							}
							DefaultTestSuite report = new DefaultTestSuite();
							report.setName(suitePath);
							for (JobExecutorHandler<J> h : getHandlers()) {
								h.startTestSuite(job, suitePath);
							}
							try {
								try {
									log.info("Launching suite: "
											+ suite.getName());
									report = SequentialExecutor.this
											.launchSuite(server, job, suite);
								} finally {
									report.setIterationCount(cycle);
								}
								if (getReportOutputProcessor() != null) {
									getReportOutputProcessor().processOutput(
											report);
								} else {
									log.debug("No report output processor configured");
								}
							} catch (OutputProcessException e) {
								throw new TestJobException(job,
										"Error during processing report output for suite: "
												+ suite.getName(), e);
							} catch (Throwable e) {
								log.error(
										"Failed to execute suite: "
												+ suite.getName(), e);
								throw new TestJobException(job,
										"Failed to execute suite: "
												+ suite.getName(), e);
							} finally {
								if (getStatus().getType() != StatusType.STOPPED) {
									for (JobExecutorHandler<J> h : getHandlers()) {
										h.finishTestSuite(job, suitePath,
												report);
									}
								}
							}
							if (getStatus().getType() != StatusType.EXECUTING_SUITES) {
								log.info("Job execution for \"" + job
										+ "\" stopped explicitly");
								break OUTER_CYCLE;
							}
						}
					} while (shouldRepeatSuites(job));
				} catch (TestJobException e) {
					if (e.getJob() == null) {
						e.setJob(job);
					}
					if (getStatus().getType() != StatusType.STOPPED) {
						log.error("Execution error", e);
					}
					throw e;
				} finally {
					synchronized (SequentialExecutor.class) {
						if (getStatus().getType() == StatusType.EXECUTING_SUITES) {
							getStatus().setType(StatusType.FINISHED_SUITES);
						}
						try {
							log.info("Finished job execution: " + job);
							for (JobExecutorHandler<J> h : getHandlers()) {
								h.finishExecution(job, getStatus());
							}
						} finally {
							if (getStatus().getType() == StatusType.STOPPING) {
								SequentialExecutor.this.stop(true, false);
							}
						}
					}
				}
			} catch (Throwable e) {
				log.error("Unknow error in executor for job: " + job, e);
				SequentialExecutor.this.getStatus().setExecutionError(
						new TestJobException(job, e.getMessage()));
			} finally {
				finsihed = true;
			}
		}
	}

	@Override
	synchronized public void init(J job) throws TestJobException {
		if (getStatus().getType() == StatusType.STOPPED) {
			this.job = job;
			init();
		} else {
			throw new TestJobException(
					"Executor has unfinished job, stop current execution before starting a new one!");
		}
	}

	synchronized private void init() throws TestJobException {
		getStatus().setType(StatusType.INITIALIZING);
		log.info("Initializing executor for job: " + job);
		if (server == null) {
			server = SequentialExecutor.this.createServerInstance(job);
			try {
				server.start();
			} catch (Exception e) {
				getStatus().setType(StatusType.STOPPED);
				throw new TestJobException(job,
						"Failed to start selenium server", e);
			}
		}
		try {
			for (JobExecutorHandler<J> h : getHandlers()) {
				h.initJob(job);
			}
		} catch (TestJobException e) {
			getStatus().setType(StatusType.STOPPED);
			throw e;
		}

		getStatus().setType(StatusType.INITIALIZED);
	}

	@Override
	synchronized public void start(List<String> suites) throws TestJobException {
		switch (getStatus().getType()) {
		case FINISHED_SUITES:
		case INITIALIZED:
			log.info("Starting execution instance for job: " + job);
			this.suites = suites;
			thread = new SequentialExecutorThread();
			setStatus(new DefaultExecutionStatus());
			getStatus().setType(StatusType.EXECUTING_SUITES);
			thread.start();
			break;
		default:
			throw new TestJobException(job,
					"Executor isn't initialized properly, has state: "
							+ getStatus().getType());
		}
	}

	@SuppressWarnings("deprecation")
	private void stop(boolean shutdown, boolean stopThread)
			throws TestJobException {
		if (getStatus().getType() == StatusType.STOPPED) {
			return;
		}
		if (shutdown || getStatus().getType() == StatusType.FINISHED_SUITES) {
			getStatus().setStoppedByUser(true);
			log.info("Shutdowning selenium server by user for job: " + job);
			getStatus().setType(StatusType.STOPPED);
			try {
				if (server != null) {
					server.stop();
					if (thread != null && stopThread) {
						thread.stop();
						while (!thread.finsihed) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								log.warn("Unexpected interrupted exception", e);
							}
						}
					}
				}
			} finally {
				server = null;
				thread = null;
				for (JobExecutorHandler<J> h : getHandlers()) {
					try {
						h.stopJob(job);
					} catch (TestJobException e) {
						log.error("Error on " + h.getClass()
								+ "#stopJob for job: " + job, e);
					}
				}
				// job = null;
			}
		} else if (getStatus().getType() == StatusType.EXECUTING_SUITES) {
			getStatus().setType(StatusType.STOPPING);
			getStatus().setStoppedByUser(true);
			log.info("Going to stop job execution by user: " + job);
		} else {
			stop(true, stopThread);
		}
	}

	@Override
	public void stop(boolean shutdown) throws TestJobException {
		stop(shutdown, true);
	}

	/**
	 * Override in supper classes to allow repetition of suite execution.
	 * 
	 * @param job
	 *            Job to repeat suites
	 * @return True if suites should be repeated again
	 */
	protected boolean shouldRepeatSuites(J job) {
		return false;
	}
}
