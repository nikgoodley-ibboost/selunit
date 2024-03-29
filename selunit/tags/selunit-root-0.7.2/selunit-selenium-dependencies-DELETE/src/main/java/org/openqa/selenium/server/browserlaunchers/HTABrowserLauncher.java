/*
 * Created on May 14, 2006
 *
 */
package org.openqa.selenium.server.browserlaunchers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.browserlaunchers.AsyncExecute;
import org.openqa.selenium.browserlaunchers.BrowserLauncher;
import org.openqa.selenium.browserlaunchers.LauncherUtils;
import org.openqa.selenium.browserlaunchers.locators.InternetExplorerLocator;
import org.openqa.selenium.os.CommandLine;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.server.RemoteControlConfiguration;

//EB - Why doesn't this class extend AbstractBrowserLauncher
//DGF - because it would override every method of ABL.
/**
 * Selunit patch: Adapted to work in a none one JAR environment; added support
 * for reading the user extensions file from RC.
 * 
 * @author mbok
 */
public class HTABrowserLauncher implements BrowserLauncher {
	static Logger log = Logger.getLogger(HTABrowserLauncher.class.getName());
	private String sessionId;
	private File dir;
	private String htaCommandPath;
	private Process htaProcess;
	private Process iexploreProcess;
	private RemoteControlConfiguration configuration;
	private Capabilities browserOptions;

	public HTABrowserLauncher(Capabilities browserOptions,
			RemoteControlConfiguration configuration, String sessionId,
			String browserLaunchLocation) {
		if (browserLaunchLocation == null) {
			browserLaunchLocation = findHTALaunchLocation();
		}
		htaCommandPath = browserLaunchLocation;
		this.sessionId = sessionId;
		this.configuration = configuration;
		this.browserOptions = browserOptions;
	}

	private static String findHTALaunchLocation() {
		String defaultPath = System.getProperty("mshtaDefaultPath");
		if (defaultPath == null) {
			defaultPath = WindowsUtils.findSystemRoot()
					+ "\\system32\\mshta.exe";
		}
		File defaultLocation = new File(defaultPath);
		if (defaultLocation.exists()) {
			return defaultLocation.getAbsolutePath();
		}
		String mshtaEXE = CommandLine.findExecutable("mshta.exe");
		if (mshtaEXE != null)
			return mshtaEXE;
		throw new RuntimeException(
				"MSHTA.exe couldn't be found in the path!\n"
						+ "Please add the directory containing mshta.exe to your PATH environment\n"
						+ "variable, or explicitly specify a path to mshta.exe like this:\n"
						+ "*mshta c:\\blah\\mshta.exe");
	}

	private void launch(String url, String htaName) {
		String query = LauncherUtils.getQueryString(url);
		query += "&baseUrl=http://localhost:" + getPort() + "/selenium-server/";
		createHTAFiles();
		String hta = (new File(dir, "core/" + htaName)).getAbsolutePath();
		log.info("Launching Embedded Internet Explorer...");
		CommandLine command = new CommandLine(new InternetExplorerLocator()
				.findBrowserLocationOrFail().launcherFilePath(), "-Embedding");
		iexploreProcess = command.executeAsync();
		log.info("Launching Internet Explorer HTA...");

		command = new CommandLine(htaCommandPath, hta, query);
		htaProcess = command.executeAsync();
	}

	/**
	 * Extracts files from classpath by using an index.properties files which
	 * list all file in core. Furthermore all JS resource locations in HTA files
	 * are changed to the RC server, as others browser do it.
	 * 
	 * @author mbok
	 */
	private void createHTAFiles() {
		dir = LauncherUtils.createCustomProfileDir(sessionId);
		File coreDir = new File(dir, "core");
		try {
			coreDir.mkdirs();
			extractCoreResourcesByIndex(HTABrowserLauncher.class, coreDir);
			File selRunnerSrc = new File(coreDir, "RemoteRunner.html");
			File selRunnerDest = new File(coreDir, "RemoteRunner.hta");
			String remRunnerStr = FileUtils.readFileToString(selRunnerSrc);
			remRunnerStr = remRunnerStr.replaceAll("src=\"",
					"src=\"http://localhost:" + configuration.getPort()
							+ "/selenium-server/core/");
			FileUtils.writeStringToFile(selRunnerDest, remRunnerStr);

			File testRunnerSrc = new File(coreDir, "TestRunner.html");
			File testRunnerDest = new File(coreDir, "TestRunner.hta");
			String testRunnerStr = FileUtils.readFileToString(testRunnerSrc);
			testRunnerStr = testRunnerStr.replaceAll("src=\"",
					"src=\"http://localhost:" + configuration.getPort()
							+ "/selenium-server/core/");
			FileUtils.writeStringToFile(testRunnerDest, testRunnerStr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Extracts files from classpath located in "/core" to destination folder as
	 * workaround to the default extraction from a jar file by
	 * {@link ResourceExtractor#extractResourcePath(Class, String, File)}, which
	 * doesn't work in multi classpath scenarios.
	 * 
	 * @param c
	 *            class to access classpath by
	 * @param dest
	 *            destination folder
	 * @throws IOException
	 */
	private void extractCoreResourcesByIndex(Class<?> c, File dest)
			throws IOException {
		InputStream indexStream = c
				.getResourceAsStream("/core/index.properties");
		if (indexStream != null) {
			Properties indexProps = new Properties();
			indexProps.load(indexStream);
			String indexStr = indexProps.getProperty("core.files");
			if (indexStr != null) {
				String[] resources = indexStr.split(";");
				log.info("Copying " + resources.length
						+ " core resource from classpath by index list...");
				for (String r : resources) {
					InputStream rIs = c.getResourceAsStream("/core/" + r);
					if (rIs != null) {
						File targetFile = new File(dest, r);
						if (r.contains("/")) {
							targetFile.getParentFile().mkdirs();
						}
						BufferedOutputStream out = new BufferedOutputStream(
								new FileOutputStream(targetFile));
						try {
							IOUtils.copy(rIs, out);
						} finally {
							out.close();
							rIs.close();
						}
					} else {
						log.warning("Couldn't read resource: " + r);
					}
				}
			}
		}
	}

	/**
	 * @author mbok: Original implementation working with one JAR and local
	 *         extracted files private void createHTAFiles() { dir =
	 *         LauncherUtils.createCustomProfileDir(sessionId); File coreDir =
	 *         new File(dir, "core"); try { coreDir.mkdirs();
	 *         ResourceExtractor.extractResourcePath(HTABrowserLauncher.class,
	 *         "/core", coreDir); File selRunnerSrc = new File(coreDir,
	 *         "RemoteRunner.html"); File selRunnerDest = new File(coreDir,
	 *         "RemoteRunner.hta"); File testRunnerSrc = new File(coreDir,
	 *         "TestRunner.html"); File testRunnerDest = new File(coreDir,
	 *         "TestRunner.hta"); // custom user-extensions File userExt =
	 *         this.configuration.getUserExtensions(); if (userExt != null) {
	 *         File selUserExt = new File(coreDir,
	 *         "scripts/user-extensions.js"); FileHandler.copy(userExt,
	 *         selUserExt); } FileHandler.copy(selRunnerSrc, selRunnerDest);
	 *         FileHandler.copy(testRunnerSrc, testRunnerDest);
	 *         writeSessionExtensionJs(coreDir); } catch (IOException e) { throw
	 *         new RuntimeException(e); } }
	 **/

	/**
	 * Writes the session extension javascript to the custom profile directory.
	 * The request for it does not pass through the Selenium server in HTA mode,
	 * thus the specialized extension js resource handler is of no use.
	 * 
	 * @param coreDir
	 * @throws IOException
	 */
	/**
	 * @author mbok private void writeSessionExtensionJs(File coreDir) throws
	 *         IOException { FrameGroupCommandQueueSet queueSet =
	 *         FrameGroupCommandQueueSet .getQueueSet(sessionId);
	 * 
	 *         if (queueSet.getExtensionJs().length() > 0) { String path =
	 *         "scripts/user-extensions.js[" + sessionId + "]"; FileWriter
	 *         fileWriter = new FileWriter(new File(coreDir, path));
	 *         BufferedWriter writer = new BufferedWriter(fileWriter);
	 * 
	 *         writer.write(queueSet.getExtensionJs()); writer.close();
	 * 
	 *         fileWriter.close(); } }
	 **/

	public void close() {
		if (browserOptions.is("killProcessesByName")) {
			WindowsUtils.tryToKillByName("iexplore.exe");
		}
		if (browserOptions.is("killProcessesByName")) {
			WindowsUtils.tryToKillByName("mshta.exe");
		}
		if (iexploreProcess != null) {
			int exitValue = AsyncExecute.killProcess(iexploreProcess);
			if (exitValue == 0) {
				log.warning("Embedded iexplore seems to have ended on its own (did we kill the real browser???)");
			}
		}
		if (htaProcess == null)
			return;
		AsyncExecute.killProcess(htaProcess);
		LauncherUtils.recursivelyDeleteDir(dir);
	}

	public Process getProcess() {
		return htaProcess;
	}

	public void launchHTMLSuite(String suiteUrl, String browserURL) {
		launch(LauncherUtils.getDefaultHTMLSuiteUrl(browserURL, suiteUrl,
				(!BrowserOptions.isSingleWindow(browserOptions)), getPort()),
				"TestRunner.hta");
	}

	private int getPort() {
		return configuration.getPortDriversShouldContact();
	}

	/**
	 * Note that the browserConfigurationOptions object is ignored; This browser
	 * configuration is not supported for IE
	 */
	public void launchRemoteSession(String url) {
		launch(LauncherUtils.getDefaultRemoteSessionUrl(url, sessionId,
				(!BrowserOptions.isSingleWindow(browserOptions)), getPort(),
				browserOptions.is("browserSideLog")), "RemoteRunner.hta");
	}

}
