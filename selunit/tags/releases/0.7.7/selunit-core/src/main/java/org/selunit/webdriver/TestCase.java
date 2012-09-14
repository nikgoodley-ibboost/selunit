package org.selunit.webdriver;

public interface TestCase extends PropertiesContext, TestCaseContext {
	public String getName();

	public Command[] getCommands();

}
