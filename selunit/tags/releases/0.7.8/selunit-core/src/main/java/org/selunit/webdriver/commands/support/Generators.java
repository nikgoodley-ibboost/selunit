package org.selunit.webdriver.commands.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selunit.webdriver.CommandInvocation;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.TestException;
import org.selunit.webdriver.commands.annotations.CommandGenerator;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.commands.annotations.ValueParam;

import com.google.common.base.Predicate;

public class Generators {

	public static final String WaitForPresent = "waitForPresent";

	@CommandGenerator(name = WaitForPresent, prefix = "waitFor", suffix = "Present")
	public void waitForPresent(final CommandInvocation<WebElement> ci,
			ExecutionContext ctx) throws TestException {
		new WebDriverWait(ctx.getWebDriver(), 5)
				.until(new Predicate<WebDriver>() {
					@Override
					public boolean apply(WebDriver driver) {
						return ci.invoke() != null;
					}
				});
	}

	public static final String WaitForValue = "waitForValue";

	@CommandGenerator(name = WaitForValue, prefix = "waitFor")
	public void waitForValueText(final CommandInvocation<Object> ci,
			ExecutionContext ctx, final @ValueParam String value)
			throws TestException {
		new WebDriverWait(ctx.getWebDriver(), 5)
				.until(new Predicate<WebDriver>() {
					@Override
					public boolean apply(WebDriver driver) {
						Object thisValue = ci.invoke();
						return thisValue != null
								&& thisValue.toString().equals(value);
					}
				});
	}

	public static final String WaitForTarget = "waitForTarget";

	@CommandGenerator(name = WaitForTarget, prefix = "waitFor")
	public void waitForTargetText(final CommandInvocation<Object> ci,
			ExecutionContext ctx, final @TargetParam String target)
			throws TestException {
		waitForValueText(ci, ctx, target);
	}

	public static final String StoreValue = "storeValue";

	@CommandGenerator(name = StoreValue, prefix = "store")
	public void storeString(ExecutionContext ctx, CommandInvocation<Object> ci,
			@ValueParam String varName) {
		Object v = ci.invoke();
		if (v != null) {
			ctx.getParamEvaluator()
					.getStoredVars(
							ctx.getPropertiesContext(ctx
									.getDefaultPropertiesScope()))
					.put(varName, v.toString());
		} else {
			ctx.getParamEvaluator()
					.getStoredVars(
							ctx.getPropertiesContext(ctx
									.getDefaultPropertiesScope()))
					.put(varName, null);
		}
	}

	public static final String VerifyValue = "verifyValue";

	@CommandGenerator(name = VerifyValue, prefix = "verify")
	public void verifyValue(final CommandInvocation<Object> ci,
			ExecutionContext ctx, final @ValueParam String value)
			throws TestException {
		Object thisValue = ci.invoke();
		if (thisValue == null || !thisValue.toString().equals(value)) {
			throw new TestException("Actual value '" + thisValue.toString()
					+ "' did not match '" + value + "'");
		}
	}

	public static final String VerifyTarget = "verifyTarget";

	@CommandGenerator(name = VerifyTarget, prefix = "verify")
	public void verifyTarget(final CommandInvocation<Object> ci,
			ExecutionContext ctx, final @TargetParam String target)
			throws TestException {
		verifyValue(ci, ctx, target);
	}
}
