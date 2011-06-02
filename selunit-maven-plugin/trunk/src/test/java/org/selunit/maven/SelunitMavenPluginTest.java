package org.selunit.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.selunit.maven.Job;
import org.selunit.maven.SelunitMavenPlugin;
import org.selunit.maven.config.EnvironmentInfoConfig;
import org.selunit.maven.config.SeleniumPropertiesConfig;


/**
 * Test for {@link SelunitMavenPlugin}.
 */

public class SelunitMavenPluginTest {

	//@Test
	public void testBasic() throws Exception {
		SelunitMavenPlugin p = new SelunitMavenPlugin();
		MavenProject mp = new MavenProject();
		mp.setName("maven-project-name");
		p.setTestResourcesDirectory(new File("src/test/resources/suites"));
		p.setProject(mp);

		Job jobC = new Job();
		jobC.setName("job-1");

		SeleniumPropertiesConfig sc = new SeleniumPropertiesConfig();
		sc.setBrowserKey("*firefox");
		sc.setBrowserUrl("http://www.google.de");
		sc.setTimeoutInSeconds(1800);
		sc.setPort(4449);
		jobC.setSeleniumProperties(sc);

		EnvironmentInfoConfig ec = new EnvironmentInfoConfig();
		ec.setOs("Android 2.3");
		ec.setBrowser("Firefox 4.1");
		jobC.setEnvironmentInfo(ec);

		jobC.setIncludeSuites(new ArrayList<String>(Collections
				.singletonList("*Suite*.html")));
		jobC.setExcludeSuites(new ArrayList<String>(Collections
				.singletonList("**/*Suite*Copy.html")));

		p.setJobs(Collections.singletonList(jobC));

		File reportsDir = new File("target/selunit-reports");
		FileUtils.deleteDirectory(reportsDir);
		p.setReportsDirectory(reportsDir);
		p.execute();
		Assert.assertEquals(1, reportsDir.list().length);
		
		p.setConvertToJunitReports(true);
		File junitReportsDir = new File("target/selunit-reports-junit");
		p.setJunitReportsDirectory(junitReportsDir);
		FileUtils.deleteDirectory(reportsDir);
		FileUtils.deleteDirectory(junitReportsDir);
		p.execute();
		Assert.assertEquals(1, reportsDir.list().length);
		Assert.assertEquals(1, junitReportsDir.list().length);
	}

	@Test
	public void testConfigInheritanceFromPlugin() throws Exception {
		SelunitMavenPlugin p = new SelunitMavenPlugin();
		MavenProject mp = new MavenProject();
		mp.setName("maven-project-name");
		p.setTestResourcesDirectory(new File("src/test/resources/suites"));
		p.setProject(mp);

		SeleniumPropertiesConfig sc = new SeleniumPropertiesConfig();
		sc.setBrowserKey("*firefox");
		sc.setBrowserUrl("http://www.google.de");
		sc.setTimeoutInSeconds(300);
		sc.setPort(4449);
		p.setSeleniumProperties(sc);

		EnvironmentInfoConfig ec = new EnvironmentInfoConfig();
		ec.setOs("Android 2.3");
		ec.setBrowser("Firefox 4.1");
		p.setEnvironmentInfo(ec);

		p.setIncludeSuites(new ArrayList<String>(Collections
				.singletonList("*Suite*.html")));
		p.setExcludeSuites(new ArrayList<String>(Collections
				.singletonList("**/*Suite*Copy.html")));

		Job jobC = new Job();
		jobC.setName("job-2");
		p.setJobs(Collections.singletonList(jobC));

		File reportsDir = new File("target/selunit-reports");
		FileUtils.deleteDirectory(reportsDir);
		p.setReportsDirectory(reportsDir);
		p.execute();
		Assert.assertEquals(1, reportsDir.list().length);

	}
}
