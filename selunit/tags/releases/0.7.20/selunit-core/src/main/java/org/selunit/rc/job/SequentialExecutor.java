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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.server.SeleniumCommandTimedOutException;
import org.selunit.job.JobExecutorHandler;
import org.selunit.job.JobStatus.StatusType;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.job.support.DefaultExecutionStatus;
import org.selunit.rc.report.server.ExtSeleniumServer;
import org.selunit.report.ResultType;
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
		private boolean killSignal = false;

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
							report.setFileName(suitePath);
							report.setStartTime(System.currentTimeMillis());
							report.setResultType(ResultType.EXECUTING);
							report.setJobInfo(job);
							for (JobExecutorHandler<J> h : getHandlers()) {
								h.startTestSuite(job, suitePath);
							}
							long startTmst = System.currentTimeMillis();
							try {
								try {
									log.info("Launching suite: "
											+ suite.getName());
									final TestResource suiteToLaunch = suite;
									class SuiteLauncherThread extends Thread {
										private Throwable exception;
										private DefaultTestSuite result;
										private boolean done = false;

										@Override
										public void run() {
											try {
												log.info("Launching suite asynchronous: "
														+ suiteToLaunch
																.getName());
												result = SequentialExecutor.this
														.launchSuite(server,
																job,
																suiteToLaunch);
											} catch (Throwable e) {
												log.error(
														"Error during launching suite: "
																+ suiteToLaunch
																		.getName(),
														e);
												exception = e;
											} finally {
												log.info("Finished suite asynchronous: "
														+ suiteToLaunch
																.getName());
												done = true;
											}
										}
									}
									SuiteLauncherThread launchThread = new SuiteLauncherThread();
									launchThread.start();
									while (!this.killSignal) {
										if (launchThread.done) {
											break;
										}
										Thread.sleep(100);
									}
									if (this.killSignal) {
										log.info("Killing suite launching thread");
										launchThread.stop();
										while (!launchThread.done) {
											log.info("Waiting for thread in: "
													+ Arrays.toString(thread
															.getStackTrace()));
											Thread.sleep(100);
										}
									}
									if (launchThread.result != null) {
										log.info("Finished suite launching with a report");
										report = launchThread.result;
									}
									if (launchThread.exception != null) {
										log.info(
												"Finished suite launching thread with an exception: ",
												launchThread.exception);
										if (launchThread.exception instanceof SeleniumCommandTimedOutException
												|| launchThread.exception
														.getCause() instanceof SeleniumCommandTimedOutException) {
											report.setResultMessage("Timeout exception for suite: "
													+ suite.getName());
											log.info(report.getResultMessage()
													+ " - job: " + job);
										}
									}
									// report =
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
								report.setResultMessage("Error during processing report output for suite: "
										+ suite.getName());
								throw new TestJobException(job,
										report.getResultMessage(), e);
							} catch (Throwable e) {
								report.setResultMessage("Failed to execute suite: "
										+ suite.getName());
								throw new TestJobException(job,
										report.getResultMessage(), e);
							} finally {
								// Finalize report if not filled properly
								if (report.getStartTime() <= 0) {
									log.warn("Missing start time in report for suite: "
											+ suite.getName());
									report.setStartTime(startTmst);
								}
								if (report.getEndTime() <= 0) {
									report.setEndTime(System
											.currentTimeMillis());
									report.setTime((report.getEndTime() - report
											.getStartTime()) / 1000);
								}
								if (report.getResultType() == ResultType.EXECUTING) {
									if (getStatus().getType() == StatusType.STOPPING
											&& this.killSignal) {
										report.setResultType(ResultType.CANCELED);
										report.setResultMessage("Canceled by user");
									} else {
										report.setResultType(ResultType.FAILED);
										if (report.getResultMessage() == null) {
											report.setResultMessage("Unresolvable termination");
										}
									}
								}
								for (JobExecutorHandler<J> h : getHandlers()) {
									h.finishTestSuite(job, suitePath, report);
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
					setConditionalState(StatusType.FINISHED_SUITES,
							StatusType.EXECUTING_SUITES);
					log.info("Finished job execution: " + job);
					for (JobExecutorHandler<J> h : getHandlers()) {
						h.finishExecution(job, getStatus());
					}
				}
			} catch (Throwable e) {
				log.error("Unknow error in executor for job: " + job, e);
				SequentialExecutor.this.getStatus().setExecutionError(
						new TestJobException(job, e.getMessage()));
			} finally {
				finsihed = true;
				if (!killSignal) {
					try {
						executeSynchronized(new Execution() {
							@Override
							public void execute() throws TestJobException {
								if (getStatus().getType() == StatusType.STOPPING) {
									SequentialExecutor.this.shutdown();
								}
							}
						});
					} catch (TestJobException e) {
						log.error("Failed to shutdown execution for job: "
								+ job, e);
					}
				}
			}
		}
	}

	protected static interface Execution {
		public void execute() throws TestJobException;
	}

	synchronized protected void executeSynchronized(final Execution exec)
			throws TestJobException {
		exec.execute();
	}

	protected void setConditionalState(final StatusType toSet,
			final StatusType... shouldHave) throws TestJobException {
		synchronized (getStatus()) {
			OUT: if (shouldHave != null && shouldHave.length > 0) {
				for (StatusType h : shouldHave) {
					if (h == getStatus().getType()) {
						break OUT;
					}
				}
				return;
			}
			getStatus().setType(toSet);
		}
	}

	@Override
	public void init(final J job) throws TestJobException {
		executeSynchronized(new Execution() {
			@Override
			public void execute() throws TestJobException {
				if (getStatus().getType() == StatusType.STOPPED) {
					SequentialExecutor.this.job = job;
					init();
				} else {
					throw new TestJobException(
							"Executor has unfinished job, stop current execution before starting a new one!");
				}
			}
		});
	}

	private void init() throws TestJobException {
		setConditionalState(StatusType.INITIALIZING);
		log.info("Initializing executor for job: " + job);
		if (server == null) {
			server = SequentialExecutor.this.createServerInstance(job);
			try {
				server.start();
			} catch (Exception e) {
				setConditionalState(StatusType.STOPPED);
				throw new TestJobException(job,
						"Failed to start selenium server", e);
			}
		}
		try {
			for (JobExecutorHandler<J> h : getHandlers()) {
				h.initJob(job);
			}
		} catch (TestJobException e) {
			setConditionalState(StatusType.STOPPED);
			throw e;
		}

		setConditionalState(StatusType.INITIALIZED);
	}

	@Override
	public void start(final List<String> suites) throws TestJobException {
		executeSynchronized(new Execution() {
			@Override
			public void execute() throws TestJobException {
				switch (getStatus().getType()) {
				case FINISHED_SUITES:
				case INITIALIZED:
					log.info("Starting execution instance for job: " + job);
					SequentialExecutor.this.suites = suites;
					thread = new SequentialExecutorThread();
					setStatus(new DefaultExecutionStatus());
					setConditionalState(StatusType.EXECUTING_SUITES);
					thread.start();
					break;
				default:
					throw new TestJobException(job,
							"Executor isn't initialized properly, has state: "
									+ getStatus().getType());
				}
			}
		});
	}

	private void shutdown() throws TestJobException {
		if (getStatus().getType() == StatusType.STOPPED) {
			log.info("Execution already stopped for job: " + job);
			return;
		}
		log.info("Shutdowning execution for job: " + job);
		setConditionalState(StatusType.STOPPING);
		try {
			if (server != null) {
				try {
					server.stop();
				} catch (Throwable e) {
					log.error("Failed to stop server for job: " + job, e);
				}
			}
			if (thread != null) {
				log.info("Killing thread for job: " + job);
				thread.killSignal = true;
				// thread.stop();
				while (!thread.finsihed) {
					try {
						log.info("Waiting for thread in: "
								+ Arrays.toString(thread.getStackTrace()));
						Thread.sleep(100);
					} catch (InterruptedException e) {
						log.warn("Unexpected interrupted exception", e);
					}
				}
			}
		} finally {
			log.info("Killed thread for job: " + job);
			server = null;
			thread = null;
			try {
				for (JobExecutorHandler<J> h : getHandlers()) {
					try {
						h.stopJob(job);
					} catch (Throwable e) {
						log.error("Error on " + h.getClass()
								+ "#stopJob for job: " + job, e);
					}
				}
			} finally {
				setConditionalState(StatusType.STOPPED);
				log.info("Stopped job execution: " + job);
			}
		}
	}

	@Override
	public void stop(final boolean shutdown) throws TestJobException {
		executeSynchronized(new Execution() {
			@Override
			public void execute() throws TestJobException {
				if (!shutdown
						&& getStatus().getType() == StatusType.EXECUTING_SUITES) {
					setConditionalState(StatusType.STOPPING);
					getStatus().setStoppedByUser(true);
					log.info("Going to stop job execution by user nicely: "
							+ job);
				} else {
					log.info("Going to shutdown job execution by user: " + job);
					getStatus().setStoppedByUser(true);
					shutdown();
				}
			}
		});
	}

	/**
	 * Override in supper classes to allow repetition of suite execution.
	 * 
	 * @param job
	 *            Job to repeat suites
	 * @return True if suites should be repeated again
	 */
	protected boolean shouldRepeatSuites(final J job) {
		return false;
	}
}
