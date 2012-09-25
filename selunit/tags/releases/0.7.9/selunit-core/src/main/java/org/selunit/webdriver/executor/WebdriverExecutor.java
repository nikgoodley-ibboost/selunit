package org.selunit.webdriver.executor;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.CommandContext;
import org.selunit.webdriver.CommandInvocation;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.TestCase;
import org.selunit.webdriver.TestCaseContext;
import org.selunit.webdriver.TestException;
import org.selunit.webdriver.TestSuite;
import org.selunit.webdriver.support.DefaultExecutionContext;

public class WebdriverExecutor {
	private CommandMappingProvider commandProvider;

	public WebdriverExecutor(CommandMappingProvider commandProvider) {
		this.commandProvider = commandProvider;
	}

	public void execute(TestSuite suite, WebDriver driver,
			Capabilities capabilities, String baseUrl)
			throws InvalidCommandException, TestException {
		Command command;
		if (suite.getTestCases().length > 0
				&& suite.getTestCases()[0].getCommands().length > 0) {
			command = suite.getTestCases()[0].getCommands()[0];
			DefaultExecutionContext ec = new DefaultExecutionContext(command,
					null, driver, capabilities, baseUrl);

			while (command != null) {
				ec.setNextCommand(getNextCommand(command));
				CommandInvocation<?> ci = createCommandInvocation(command, ec);
				ci.invoke();
				command = ec.getNextCommand();
			}
		}
	}

	protected CommandInvocation<?> createCommandInvocation(Command c,
			ExecutionContext ec) throws InvalidCommandException {
		return commandProvider.create(c, ec);
	}

	protected Command getNextCommand(Command c) {
		Command nc = null;
		CommandContext cc = c;
		TestCaseContext tc = c.getTestCase();
		if (cc.getCommandIndex() + 1 < cc.getTestCase().getCommands().length) {
			// Next from current test case
			nc = cc.getTestCase().getCommands()[cc.getCommandIndex() + 1];
		} else if (tc.getTestCaseIndex() + 1 < tc.getSuite().getTestCases().length) {
			// Next from next test case
			TestCase newCase = tc.getSuite().getTestCases()[tc
					.getTestCaseIndex() + 1];
			nc = newCase.getCommands()[0];
		}
		return nc;
	}
}
