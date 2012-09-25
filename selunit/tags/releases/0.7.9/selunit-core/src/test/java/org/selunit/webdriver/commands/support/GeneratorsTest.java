package org.selunit.webdriver.commands.support;

import org.junit.Before;
import org.junit.Test;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.TestException;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.executor.CommandMappingProvider;
import org.selunit.webdriver.support.DefaultCommand;
import org.selunit.webdriver.support.DefaultExecutionContext;

public class GeneratorsTest {
	private CommandMappingProvider cmdProvider;
	private DefaultExecutionContext ctx = new DefaultExecutionContext(null,
			null, null, null, null);

	@Before
	public void setUp() throws Exception {
		cmdProvider = new CommandMappingProvider();
		cmdProvider.scanForCommands("org.selunit.webdriver");
	}

	@Test
	public void testVerifyTargetOk() throws TestException,
			InvalidCommandException {
		cmdProvider.create(
				new DefaultCommand("verifyTargetCommand", "test", null), ctx)
				.invoke();
	}

	@Test(expected = TestException.class)
	public void testVerifyTargetNOk() throws TestException,
			InvalidCommandException {
		cmdProvider.create(
				new DefaultCommand("verifyTargetCommand", "!test", null), ctx)
				.invoke();
	}

	@Test
	public void testVerifyValueOk() throws TestException,
			InvalidCommandException {
		cmdProvider.create(
				new DefaultCommand("verifyValueCommand", null, "test"), ctx)
				.invoke();
	}

	@Test(expected = TestException.class)
	public void testVerifyValueNOk() throws TestException,
			InvalidCommandException {
		cmdProvider.create(
				new DefaultCommand("verifyValueCommand", null, "!test"), ctx)
				.invoke();
	}

	@CommandMappings(mappings = {
			@CommandMapping(name = "targetCommand", generators = { Generators.VerifyTarget }),
			@CommandMapping(name = "valueCommand", generators = { Generators.VerifyValue }) })
	public String testCommand() {
		return "test";
	}
}
