package org.selunit.webdriver.support;

import junit.framework.Assert;

import org.junit.Test;
import org.selunit.webdriver.support.js.JavaScriptEvaluator;

public class JavaScriptEvaluatorTest {
	@Test
	public void testSimple() throws Exception {
		JavaScriptEvaluator ev = new JavaScriptEvaluator();
		DefaultExecutionContext ectx = new DefaultExecutionContext();
		ectx.getProperties().put("hello", "WebDriver");
		Assert.assertEquals(
				"Hello WebDriver",
				ev.evaluate(ectx,
						"javascript{storedVars.hello='Hello ' + storedVars.hello; storedVars.hello}"));
		Assert.assertEquals("Hello WebDriver",
				ev.evaluate(ectx, "javascript{storedVars.hello}"));
		Assert.assertEquals(
				"a3",
				ev.evaluate(new DefaultExecutionContext(),
						"javascript{storedVars.c=new Array();storedVars.c.push('a3');storedVars.c[0];}"));
		System.out
				.println(ev
						.evaluate(
								new DefaultExecutionContext(),
								"now=javascript{var date = new Date(); date.setDate(date.getDate()+1); var months = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'); months[date.getMonth()] + ' ' + date.getDate() + ', ' + date.getFullYear();}; 1+1=javascript{1+1}"));
	}
}
