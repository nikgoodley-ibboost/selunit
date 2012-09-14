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
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.browserlaunchers.ResourceExtractor;

/**
 * Util class to handle some Selenium limitations regarding core resources
 * (/core) for several browser launchers.
 * 
 * @author mbok
 * 
 */
public class CoreResourcesUtils {
	static Logger log = Logger.getLogger(CoreResourcesUtils.class.getName());

	/**
	 * Replaces all links in runner HTML files "RemoteRunner.html" and
	 * "TestRunner.html" by an absolute prefix referencing current Selenium
	 * server instance.
	 * 
	 * @param coreDir
	 *            directory containing runner HTML files
	 * @param configuration
	 *            current server configuration
	 * @throws IOException
	 *             in case of errors
	 */
	public static void routeRunnersLinksToSeleniumServer(File coreDir,
			RemoteControlConfiguration configuration) throws IOException {
		routeRunnerLinksToSeleniumServer(
				new File(coreDir, "RemoteRunner.html"), configuration);
		routeRunnerLinksToSeleniumServer(new File(coreDir, "TestRunner.html"),
				configuration);
	}

	/**
	 * Replaces all links in given runner file (if exists) by an absolute prefix
	 * referencing current Selenium server instance.
	 * 
	 * @param runner
	 *            runner html file
	 * @param configuration
	 *            server configuration
	 * @throws IOException
	 *             in case of errors
	 */
	private static void routeRunnerLinksToSeleniumServer(File runner,
			RemoteControlConfiguration configuration) throws IOException {
		if (runner.exists()) {
			String runnerStr = FileUtils.readFileToString(runner);
			runnerStr = runnerStr.replaceAll("src=\"",
					"src=\"http://localhost:" + configuration.getPort()
							+ "/selenium-server/core/");
			FileUtils.writeStringToFile(runner, runnerStr);
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
	public static void extractCoreResourcesByIndex(Class<?> c, File dest)
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
}
