package org.selunit.webdriver.commands.support;

import static org.mockito.Mockito.spy;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.TestException;
import org.selunit.webdriver.executor.CommandMappingProvider;
import org.selunit.webdriver.support.DefaultCommand;
import org.selunit.webdriver.support.DefaultExecutionContext;

public class WindowsTest {
	private CommandMappingProvider commandProvider;
	private DefaultExecutionContext executionContext;

	@Before
	public void setUp() throws InvalidCommandException {
		commandProvider = new CommandMappingProvider();
		commandProvider.scanForCommands("org.selunit.webdriver.commands");
		commandProvider = spy(commandProvider);
		executionContext = new DefaultExecutionContext();
		executionContext.setBaseUrl(new File("src/test/resources").toURI()
				.toString());
		executionContext.setWebDriver(new FirefoxDriver());
	}

	@Test
	public void testOpen() throws Exception {
		DefaultCommand cmd = new DefaultCommand("open", "pages/windows.html",
				null);
		commandProvider.create(cmd, executionContext).invoke();
		Assert.assertEquals("Windows", executionContext.getWebDriver()
				.getTitle());
	}

	@Test
	public void testTitleCommands() throws Exception {
		DefaultCommand open = new DefaultCommand("open", "pages/windows.html",
				null);
		commandProvider.create(open, executionContext).invoke();
		Assert.assertEquals("Windows", executionContext.getWebDriver()
				.getTitle());
		DefaultCommand waitForTitle = new DefaultCommand("waitForTitle",
				"Windows NEW", null);
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Assert.fail();
				}
				By.cssSelector(".change-title")
						.findElement(executionContext.getWebDriver()).click();
			}

		}.start();
		commandProvider.create(waitForTitle, executionContext).invoke();

		DefaultCommand verifyTitle = new DefaultCommand("verifyTitle",
				"Windows NEW", null);
		commandProvider.create(verifyTitle, executionContext).invoke();

		// Wait until timeout
		long start = System.currentTimeMillis();
		waitForTitle.getParams().put(Command.PARAM_TARGET, "unknown title");
		try {
			commandProvider.create(waitForTitle, executionContext).invoke();
		} catch (TestException e) {
			Assert.assertTrue(System.currentTimeMillis() - start >= 3000);
			return;
		}
		Assert.fail("Timeout exception was expected");
	}

	@After
	public void tearDown() {
		executionContext.getWebDriver().close();
	}
}
