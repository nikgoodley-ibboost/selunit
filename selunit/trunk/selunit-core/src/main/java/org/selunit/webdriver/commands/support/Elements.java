package org.selunit.webdriver.commands.support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.executor.LocatorFactory;

public class Elements {

	@CommandMappings(mappings = { @CommandMapping(name = "element", generators = { Generators.WaitForPresent }) })
	public WebElement getElement(ExecutionContext ctx,
			@TargetParam String target) {
		return LocatorFactory.getLocator(target)
				.findElement(ctx.getWebDriver());
	}

	public String getText(@TargetParam By target) {
		return null;
	}

}
