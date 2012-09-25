package org.selunit.webdriver.commands.support;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.commands.annotations.ValueParam;

/**
 * Commands regarding actions.
 * 
 * @author mbok
 * 
 */
public class ActionCommands {
	@CommandMapping(name = "click")
	public void click(ExecutionContext ctx, @TargetParam By target) {
		ctx.getWebDriver().findElement(target).click();
	}

	@CommandMapping(name = "clickAt")
	public void clickAt(ExecutionContext ctx, @TargetParam By target,
			@ValueParam String coord) {
		Point p = CommandUtils.getPoint(coord);
		new Actions(ctx.getWebDriver())
				.moveToElement(ctx.getWebDriver().findElement(target), p.x, p.y)
				.click().build().perform();
	}

	@CommandMapping(name = "doubleClick")
	public void doubleClick(ExecutionContext ctx, @TargetParam By target) {
		new Actions(ctx.getWebDriver()).doubleClick(
				ctx.getWebDriver().findElement(target)).perform();
	}

	@CommandMapping(name = "doubleClickAt")
	public void doubleClickAt(ExecutionContext ctx, @TargetParam By target,
			@ValueParam String coord) {
		Point p = CommandUtils.getPoint(coord);
		new Actions(ctx.getWebDriver())
				.moveToElement(ctx.getWebDriver().findElement(target), p.x, p.y)
				.doubleClick().perform();
	}

	@CommandMapping(name = "contextMenu")
	public void contextMenu(ExecutionContext ctx, @TargetParam By target) {
		new Actions(ctx.getWebDriver()).contextClick(
				ctx.getWebDriver().findElement(target)).perform();
	}

	@CommandMapping(name = "focus")
	public void focus(ExecutionContext ctx, @TargetParam By target) {
		new Actions(ctx.getWebDriver()).sendKeys(
				ctx.getWebDriver().findElement(target), "").perform();
	}
}
