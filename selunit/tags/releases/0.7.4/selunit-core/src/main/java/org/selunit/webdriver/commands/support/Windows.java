package org.selunit.webdriver.commands.support;

import java.net.MalformedURLException;
import java.net.URL;

import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.commands.annotations.TargetParam;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

public class Windows {
	@CommandMapping(name = "open")
	public void open(ExecutionContext ctx, @TargetParam String target) {
		String url = null;
		try {
			URL absoluteTarget = new URL(target);
			url = absoluteTarget.toString();
		} catch (MalformedURLException e) {
			// Target looks to be relative
			url = UrlUtils.resolveUrl(ctx.getBaseUrl(), target);
		}
		ctx.getWebDriver().get(url);
	}

	@CommandMappings(mappings = { @CommandMapping(name = "title", generators = {
			Generators.WaitForTarget, Generators.VerifyTarget }) })
	public String getTitle(ExecutionContext ctx) {
		return ctx.getWebDriver().getTitle();
	}
}
