package org.selunit.webdriver.commands.support;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.WebdriverTestHelper;
import org.selunit.webdriver.support.DefaultExecutionContext;

public class ActionsTest {
	private DefaultExecutionContext ctx;

	@Before
	public void setUp() throws InvalidCommandException {
		ctx = WebdriverTestHelper.createDefaultTestContext();
		new Windows().open(ctx, "pages/actions.html");
	}

	@Test
	public void testFocus() {
		new ActionCommands().focus(ctx, By.cssSelector(".focus-event"));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("Got focus", ctx.getWebDriver().getTitle());
	}

	@Test
	public void testClick() {
		new ActionCommands().click(ctx, By.cssSelector(".click-events"));
		Assert.assertEquals("Got click", ctx.getWebDriver().getTitle());
	}

	@Test
	public void testDblClick() {
		new ActionCommands().doubleClick(ctx, By.cssSelector(".click-events"));
		Assert.assertEquals("Got dblclick", ctx.getWebDriver().getTitle());
	}

	@Test
	public void testContextMenu() {
		new ActionCommands().contextMenu(ctx, By.cssSelector(".click-events"));
		Assert.assertEquals("Got contextmenu", ctx.getWebDriver().getTitle());
	}

	@After
	public void tearDown() {
		ctx.getWebDriver().close();
	}
}
