package org.selunit.webdriver.commands.support;

import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.support.js.JavaScriptEvaluator;

public class Expressions {
	@CommandMappings(mappings = { @CommandMapping(name = "storeExpression", generators = { Generators.StoreValue }) })
	public String getExpression(ExecutionContext ctx,
			@TargetParam String expression) {
		return JavaScriptEvaluator.eval(
				ctx.getPropertiesContext(ctx.getDefaultPropertiesScope()),
				expression);
	}
}
