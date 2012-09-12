package org.selunit.webdriver.executor;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.CommandInvocation;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.commands.annotations.CommandGenerator;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.commands.annotations.ValueParam;
import org.selunit.webdriver.support.DefaultCommand;
import org.selunit.webdriver.support.DefaultExecutionContext;

public class CommandMappingProviderTest {
	@Test
	public void testScanningNothing() throws Exception {
		CommandMappingProvider provider = new CommandMappingProvider();
		provider.scanForCommands("nothing");
	}

	@Test(expected = InvalidCommandException.class)
	public void testScanningNothingCreate() throws Exception {
		CommandMappingProvider provider = new CommandMappingProvider();
		provider.scanForCommands("nothing");
		provider.create(new DefaultCommand("do", null, null), null);
	}

	@Test
	public void testSimpleCommands() throws Exception {
		DefaultExecutionContext ctx = new DefaultExecutionContext(null, null,
				null, null, null);
		CommandMappingProvider provider = new CommandMappingProvider();
		CommandMappingProviderTest mock = spy(new CommandMappingProviderTest());
		provider.setMethodObject(CommandMappingProviderTest.class, mock);
		provider.scanForCommands("org.selunit.webdriver");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(Command.PARAM_TARGET, "t");
		params.put(Command.PARAM_VALUE, "v");
		Assert.assertEquals(
				"t+v",
				provider.create(
						new DefaultCommand("testCommandByMapping", params,
								null, -1), ctx).invoke());
		verify(mock).commandByMapping("t", "v", ctx);

		params.put(Command.PARAM_VALUE, "v2");
		Assert.assertEquals(
				"t+v2",
				provider.create(
						new DefaultCommand("testCommandByMappings1", params,
								null, -1), ctx).invoke());
		verify(mock).commandByMapping("t", "v2", ctx);

		params.put(Command.PARAM_VALUE, "v3");
		Assert.assertEquals(
				"t+v3",
				provider.create(
						new DefaultCommand("testCommandByMappings2", params,
								null, -1), ctx).invoke());
		verify(mock).commandByMapping("t", "v3", ctx);
	}

	@Test
	public void testWrappedCommands() throws Exception {
		DefaultExecutionContext ctx = new DefaultExecutionContext(null, null,
				null, null, null);
		CommandMappingProvider provider = new CommandMappingProvider();
		CommandMappingProviderTest mock = spy(new CommandMappingProviderTest());
		provider.setMethodObject(CommandMappingProviderTest.class, mock);
		provider.scanForCommands("org.selunit.webdriver");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put(Command.PARAM_TARGET, "t");
		params.put(Command.PARAM_VALUE, "v");
		Assert.assertEquals(
				"w1=t+v",
				provider.create(
						new DefaultCommand("g1CommandGeneratedOnceSuf", params,
								null, -1), ctx).invoke());
		verify(mock).commandByMapping("t", "v", ctx);

		params.put(Command.PARAM_VALUE, "v2");
		Assert.assertEquals(
				"w2=t+v2",
				provider.create(
						new DefaultCommand("g2CommandGeneratedTwiceSuf",
								params, null, -1), ctx).invoke());
		verify(mock).commandByMapping("t", "v2", ctx);

		Assert.assertEquals(
				"by:target+by:value",
				provider.create(
						new DefaultCommand("testCommandLocatorResolved",
								"css=target", "xpath=value", null, -1), ctx)
						.invoke());
	}

	@CommandMapping(name = "testCommandLocatorResolved")
	public String commandWithLocatorResolving(@TargetParam By target, @ValueParam By value) {
		Assert.assertNotNull(target);
		Assert.assertNotNull(value);
		return "by:target+by:value";
	}

	@CommandMappings(mappings = {
			@CommandMapping(name = "testCommandByMappings1"),
			@CommandMapping(name = "testCommandByMappings2"),
			@CommandMapping(name = "commandGeneratedOnce", generators = { "generator1" }),
			@CommandMapping(name = "commandGeneratedTwice", generators = {
					"generator1", "generator2" }) })
	@CommandMapping(name = "testCommandByMapping")
	public String commandByMapping(@TargetParam String target,
			@ValueParam String value, ExecutionContext ctx) {
		return target + "+" + value;
	}

	@CommandGenerator(name = "generator1", prefix = "g1", suffix = "Suf")
	public String wrapperMethod1(CommandInvocation<?> innerCi,
			@TargetParam String target, @ValueParam String value,
			ExecutionContext ctx) {
		return "w1=" + innerCi.invoke();
	}

	@CommandGenerator(name = "generator2", prefix = "g2", suffix = "Suf")
	public String wrapperMethod2(CommandInvocation<?> innerCi,
			@TargetParam String target, @ValueParam String value,
			ExecutionContext ctx) {
		return "w2=" + innerCi.invoke();
	}
}
