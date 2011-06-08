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
package org.selunit.maven;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.selunit.config.support.DefaultEnvironmentInfo;
import org.selunit.config.support.DefaultSeleniumProperties;
import org.selunit.job.JobExecutor;
import org.selunit.job.JobStatus.StatusType;
import org.selunit.job.TestJob;
import org.selunit.job.TestJobException;
import org.selunit.job.support.DefaultTestJob;
import org.selunit.job.support.SequentialExecutor;
import org.selunit.maven.config.EnvironmentInfoConfig;
import org.selunit.maven.config.SeleniumPropertiesConfig;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.output.OutputProcessException;
import org.selunit.report.output.OutputProcessor;
import org.selunit.report.output.OutputStreamFactory;
import org.selunit.report.output.xml.XMLOutputHandler;
import org.selunit.report.output.xml.XMLProcessor;
import org.selunit.support.DefaultTestProject;
import org.selunit.testpackage.file.DirectoryFileAccess;

/**
 * Selunit maven plugin to execute Selenium test jobs based on Selenese HTML
 * suites.
 * 
 * @requiresDependencyResolution test
 * @goal test
 * @phase test
 */

public class SelunitMavenPlugin extends AbstractMojo {
	private static int jobCounter = 0;

	/**
	 * Set this to "true" to disable test execution for all jobs.
	 * 
	 * @parameter default-value="false" expression="${maven.test.skip}"
	 */
	private boolean skip;

	/**
	 * The directory containing all test relevant resources considering Selenium
	 * suites, test cases or user extension JavaScript file of the project being
	 * tested.
	 * 
	 * @parameter default-value="${project.basedir}/src/test/resources"
	 */
	private File testResourcesDirectory;

	/**
	 * The Maven Project Object.
	 * 
	 * @parameter default-value="${project}"
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Base directory where all reports are written to.
	 * 
	 * @parameter default-value="${project.build.directory}/selunit-reports"
	 */
	private File reportsDirectory;

	/**
	 * Set this to "true" or "false" to enable/disable converting of Selunit
	 * reports to JUnit reports format.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean convertToJunitReports;

	/**
	 * Set this to "true" to write the Selenium HTML log as plain text to Junit
	 * reports, because most of JUnit report consumers don't accept HTML content
	 * as failure message and would escape it. If "false" is set the original
	 * HTML log is used as message content for JUnit failures.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean htmlLogAsTextInJunit;

	/**
	 * Directory where to JUnit format converted reports are written to.
	 * 
	 * @parameter default-value="${project.build.directory}/surefire-reports"
	 */
	private File junitReportsDirectory;

	/**
	 * Definition of test jobs to execute. For more details see the <a
	 * href="Job-mojo.html">Job</a> bean.
	 * 
	 * @parameter
	 * @required
	 */
	private List<Job> jobs;

	/**
	 * Selenium properties to use in all jobs as default. For more details see
	 * the <a
	 * href="SeleniumPropertiesConfig-mojo.html">SeleniumPropertiesConfig</a>
	 * bean.
	 * 
	 * @parameter
	 */
	private SeleniumPropertiesConfig seleniumProperties;

	/**
	 * Environment info to use in all jobs as default. For more details see the
	 * <a href="EnvironmentInfoConfig-mojo.html">EnvironmentInfoConfig</a> bean.
	 * 
	 * @parameter
	 */
	private EnvironmentInfoConfig environmentInfo;

	/**
	 * A list of &lt;include>...&lt;/include> elements pointing the Selenium
	 * test suites (by pattern) that should be included in all jobs by default
	 * if unspecified.
	 * 
	 * @parameter
	 */
	private List<String> includeSuites;

	/**
	 * A list of &lt;exclude>...&lt;/exclude> elements pointing the Selenium
	 * test suites (by pattern) that should be excluded in all jobs by default
	 * if unspecified.
	 * 
	 * @parameter
	 */
	private List<String> excludeSuites;

	/**
	 * @readonly
	 * @parameter default-value="${os.name} ${os.arch} ${os.version}"
	 */
	private String osFingerPrint;

	protected String normalizeFileNamePart(String part) {
		return part.replaceAll("[^\\w-_]", "_").replaceAll("__+", "_");
	}

	private String getHierarchicalTestName(TestSuiteReport report) {
		return normalizeFileNamePart(report.getJobInfo().getEnvironment()
				.getOs())
				+ "."
				+ normalizeFileNamePart(report.getJobInfo().getEnvironment()
						.getBrowser())
				+ "."
				+ normalizeFileNamePart(report.getJobInfo().getName())
				+ "."
				+ normalizeFileNamePart(report.getName());
	}

	private String getReportFileName(TestSuiteReport report) {
		return "TEST-" + getHierarchicalTestName(report) + ".xml";
	}

	protected JobExecutor<TestJob> getExecutor() {
		SequentialExecutor<TestJob> executor = new SequentialExecutor<TestJob>();

		// Test resources access
		DirectoryFileAccess resourcesAccess = new DirectoryFileAccess(
				getTestResourcesDirectory());
		executor.setTestResourceAccess(resourcesAccess);

		// Report processor
		final XMLProcessor xmlReportProcessor = new XMLProcessor();
		xmlReportProcessor
				.setOutputStreamFactory(new OutputStreamFactory<TestSuiteReport>() {
					@Override
					public OutputStream createReportOutputStream(
							TestSuiteReport suite) throws IOException {
						return new BufferedOutputStream(new FileOutputStream(
								new File(getReportsDirectory(),
										getReportFileName(suite))));
					}
				});
		xmlReportProcessor.getHandler().add(
				new XMLOutputHandler<TestSuiteReport>() {
					@Override
					public void handleOutput(TestSuiteReport report,
							Document jdom) throws IOException {
						Element jobInfoNode = (Element) jdom.getDescendants(
								new ElementFilter("job-info")).next();
						Element prop = new Element("prop");
						prop.setAttribute("name", "hierarchicalTestName");
						prop.setAttribute("value",
								getHierarchicalTestName(report));
						jobInfoNode.addContent(prop);
					}
				});

		executor.setReportOutputProcessor(new OutputProcessor<TestSuiteReport>() {
			@Override
			public void processOutput(TestSuiteReport suite)
					throws OutputProcessException {
				xmlReportProcessor.processOutput(suite);
				if (isConvertToJunitReports()) {
					try {
						JUnitReportConverter converter = new JUnitReportConverter();
						converter
								.setConvertHtmlLog2Text(isHtmlLogAsTextInJunit());
						String reportFileName = getReportFileName(suite);
						getLog().info(
								"Converting report to Junit format: "
										+ new File(getJunitReportsDirectory(),
												reportFileName).getName());
						converter.convert(
								new BufferedInputStream(new FileInputStream(
										new File(getReportsDirectory(),
												reportFileName))),
								new BufferedOutputStream(new FileOutputStream(
										new File(getJunitReportsDirectory(),
												reportFileName))));
					} catch (Exception e) {
						throw new OutputProcessException(
								"Failed to convert report to JUnit format", e);
					}
				}
			}
		});

		return executor;
	}

	protected TestJob createTestJob(Job jobConfig)
			throws MojoExecutionException {
		if (getIncludeSuites() != null && !jobConfig.isIncludesSet()) {
			jobConfig.setIncludeSuites(getIncludeSuites());
		}
		if (getExcludeSuites() != null && !jobConfig.isExcludesSet()) {
			jobConfig.setExcludeSuites(getExcludeSuites());
		}

		DefaultTestJob job = new DefaultTestJob();
		DirectoryScanner suitesScanner = new DirectoryScanner();
		suitesScanner.setBasedir(getTestResourcesDirectory());
		suitesScanner.setIncludes(jobConfig.getIncludeSuites().toArray(
				new String[0]));
		suitesScanner.setExcludes(jobConfig.getExcludeSuites().toArray(
				new String[0]));
		suitesScanner.scan();
		job.setSuites(Arrays.asList(suitesScanner.getIncludedFiles()));

		job.setName(jobConfig.getName() != null ? jobConfig.getName() : "job-"
				+ (jobCounter++) + "-" + getProject().getName());
		job.setId(normalizeFileNamePart(job.getName()));

		DefaultTestProject proj = new DefaultTestProject();
		proj.setName(getProject().getName());
		proj.setId(getProject().getName());
		job.setProject(proj);

		if (jobConfig.getEnvironmentInfo() != null
				|| getEnvironmentInfo() != null) {
			EnvironmentInfoConfig envInfo;
			if (jobConfig.getEnvironmentInfo() != null
					&& getEnvironmentInfo() != null) {
				envInfo = new EnvironmentInfoConfig(getEnvironmentInfo(),
						jobConfig.getEnvironmentInfo());
			} else if (getEnvironmentInfo() != null) {
				envInfo = getEnvironmentInfo();
			} else {
				envInfo = jobConfig.getEnvironmentInfo();
			}
			job.setEnvironment(new DefaultEnvironmentInfo(envInfo.getOs(),
					envInfo.getBrowser()));
		} else {
			job.setEnvironment(new DefaultEnvironmentInfo("", ""));
		}

		if (job.getEnvironment().getOs() == null
				|| StringUtils.isBlank(job.getEnvironment().getOs())) {
			((DefaultEnvironmentInfo) job.getEnvironment())
					.setOs(getOsFingerPrint());
		}

		if (getSeleniumProperties() == null
				&& jobConfig.getSeleniumProperties() == null) {
			throw new MojoExecutionException(
					"Incomplete job ("
							+ job.getName()
							+ ") configuration: Required <seleniumProperties /> configuration part is not present!");
		}
		SeleniumPropertiesConfig selConf;
		if (getSeleniumProperties() != null
				&& jobConfig.getSeleniumProperties() != null) {
			selConf = new SeleniumPropertiesConfig(getSeleniumProperties(),
					jobConfig.getSeleniumProperties());
		} else if (getSeleniumProperties() != null) {
			selConf = getSeleniumProperties();
		} else {
			selConf = jobConfig.getSeleniumProperties();
		}

		if (selConf.getBrowserUrl() == null) {
			throw new MojoExecutionException(
					"Incomplete Selenium properties configuration for job ("
							+ job.getName()
							+ "): <browserUrl>...</browserUrl> missed!");
		}

		DefaultSeleniumProperties selProps = new DefaultSeleniumProperties();
		selProps.setBrowserKey(selConf.getBrowserKey());
		selProps.setBrowserURL(selConf.getBrowserUrl());
		selProps.setTimeoutInSeconds(selConf.getTimeoutInSeconds());
		selProps.setMultiWindow(selConf.isMultiWindow());
		selProps.setPort(selConf.getPort());
		selProps.setUserExtensions(selConf.getUserExtensions());
		selProps.setBrowserCapabilities(selConf.getBrowserCapabilities());

		job.setSeleniumProperties(selProps);

		if (selProps.getBrowserKey() != null
				&& (job.getEnvironment().getBrowser() == null || StringUtils
						.isBlank(job.getEnvironment().getBrowser()))) {
			((DefaultEnvironmentInfo) job.getEnvironment()).setBrowser(selProps
					.getBrowserKey().substring(1));
		}

		return job;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (isSkip()) {
			getLog().info("Test execution is disabled explicitly for all jobs");
			return;
		}
		getReportsDirectory().mkdirs();
		if (isConvertToJunitReports()) {
			getJunitReportsDirectory().mkdirs();
		}

		getLog().info("Preparing jobs configuration...");
		ArrayList<TestJob> jobs = new ArrayList<TestJob>();
		for (Job jobConfig : getJobs()) {
			if (!jobConfig.isSkip()) {
				jobs.add(createTestJob(jobConfig));
			} else {
				getLog().info("Skip execution of job: " + jobConfig.getName());
			}
		}

		JobExecutor<TestJob> executor = getExecutor();
		try {
			for (TestJob job : jobs) {
				getLog().info(
						"Instantiating test job execution: " + job.getName());
				executor.init(job);
				getLog().info(
						"Executing suites for job '" + job.getName() + "': "
								+ job.getSuites());
				executor.start(job.getSuites());
				try {
					while (executor.getStatus().getType() == StatusType.EXECUTING_SUITES) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							throw new MojoExecutionException(
									"Thread error during tests", e);
						}
					}
				} finally {
					executor.stop(true);
				}
				getLog().info("Finished job: " + job.getName());

			}
		} catch (TestJobException e) {
			throw new MojoExecutionException("Failed during executing tests", e);
		}
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	/**
	 * @return the project
	 */
	public MavenProject getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(MavenProject project) {
		this.project = project;
	}

	/**
	 * @return the reportsDirectory
	 */
	public File getReportsDirectory() {
		return reportsDirectory;
	}

	/**
	 * @param reportsDirectory
	 *            the reportsDirectory to set
	 */
	public void setReportsDirectory(File reportsDirectory) {
		this.reportsDirectory = reportsDirectory;
	}

	/**
	 * @return the testResourcesDirectory
	 */
	public File getTestResourcesDirectory() {
		return testResourcesDirectory;
	}

	/**
	 * @param testResourcesDirectory
	 *            the testResourcesDirectory to set
	 */
	public void setTestResourcesDirectory(File testResourcesDirectory) {
		this.testResourcesDirectory = testResourcesDirectory;
	}

	/**
	 * @return the jobs
	 */
	public List<Job> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs
	 *            the jobs to set
	 */
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	/**
	 * @return the seleniumProperties
	 */
	public SeleniumPropertiesConfig getSeleniumProperties() {
		return seleniumProperties;
	}

	/**
	 * @param seleniumProperties
	 *            the seleniumProperties to set
	 */
	public void setSeleniumProperties(
			SeleniumPropertiesConfig seleniumProperties) {
		this.seleniumProperties = seleniumProperties;
	}

	/**
	 * @return the environmentInfo
	 */
	public EnvironmentInfoConfig getEnvironmentInfo() {
		return environmentInfo;
	}

	/**
	 * @param environmentInfo
	 *            the environmentInfo to set
	 */
	public void setEnvironmentInfo(EnvironmentInfoConfig environmentInfo) {
		this.environmentInfo = environmentInfo;
	}

	/**
	 * @return the includeSuites
	 */
	public List<String> getIncludeSuites() {
		return includeSuites;
	}

	/**
	 * @param includeSuites
	 *            the includeSuites to set
	 */
	public void setIncludeSuites(List<String> includeSuites) {
		this.includeSuites = includeSuites;
	}

	/**
	 * @return the excludeSuites
	 */
	public List<String> getExcludeSuites() {
		return excludeSuites;
	}

	/**
	 * @param excludeSuites
	 *            the excludeSuites to set
	 */
	public void setExcludeSuites(List<String> excludeSuites) {
		this.excludeSuites = excludeSuites;
	}

	/**
	 * @return the osFingerPrint
	 */
	public String getOsFingerPrint() {
		return osFingerPrint;
	}

	/**
	 * @param osFingerPrint
	 *            the osFingerPrint to set
	 */
	public void setOsFingerPrint(String osFingerPrint) {
		this.osFingerPrint = osFingerPrint;
	}

	/**
	 * @return the convertToJunitReports
	 */
	public boolean isConvertToJunitReports() {
		return convertToJunitReports;
	}

	/**
	 * @param convertToJunitReports
	 *            the convertToJunitReports to set
	 */
	public void setConvertToJunitReports(boolean convertToJunitReports) {
		this.convertToJunitReports = convertToJunitReports;
	}

	/**
	 * @return the junitReportsDirectory
	 */
	public File getJunitReportsDirectory() {
		return junitReportsDirectory;
	}

	/**
	 * @param junitReportsDirectory
	 *            the junitReportsDirectory to set
	 */
	public void setJunitReportsDirectory(File junitReportsDirectory) {
		this.junitReportsDirectory = junitReportsDirectory;
	}

	/**
	 * @return the htmlLogAsTextInJunit
	 */
	public boolean isHtmlLogAsTextInJunit() {
		return htmlLogAsTextInJunit;
	}

	/**
	 * @param htmlLogAsTextInJunit
	 *            the htmlLogAsTextInJunit to set
	 */
	public void setHtmlLogAsTextInJunit(boolean htmlLogAsTextInJunit) {
		this.htmlLogAsTextInJunit = htmlLogAsTextInJunit;
	}

}
