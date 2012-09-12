package org.selunit.webdriver.support;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.TestCase;
import org.selunit.webdriver.TestSuite;

public class StringSubstitutionEvaluatorTest {
	private ExecutionContext ctx;
	private HashMap<String, Object> caseParameters = new HashMap<String, Object>(),
			suiteParameters = new HashMap<String, Object>();

	@Before
	public void setUp() {
		ctx = mock(ExecutionContext.class);
		when(ctx.getCurrentCommand()).thenReturn(mock(Command.class));
		when(ctx.getCurrentCommand().getTestCase()).thenReturn(
				mock(TestCase.class));
		when(ctx.getCurrentCommand().getTestCase().getProperties()).thenReturn(
				caseParameters);

		when(ctx.getCurrentCommand().getTestCase().getSuite()).thenReturn(
				mock(TestSuite.class));
		when(ctx.getCurrentCommand().getTestCase().getSuite().getProperties())
				.thenReturn(suiteParameters);
	}

	@Test
	public void testNoSubstitution() {
		StringSubstitutionEvaluator e = new StringSubstitutionEvaluator();
		Assert.assertEquals("hallo abc",
				e.evaluate(ctx.getCurrentCommand().getTestCase(), "hallo abc"));
	}
}
