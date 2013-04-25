package org.selunit.webdriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public interface ExecutionContext {
	public Command getCurrentCommand();

	public Command getNextCommand();

	public void setNextCommand(Command next);

	public WebDriver getWebDriver();

	public Capabilities getCapabilities();

	public String getBaseUrl();

	public ParamEvaluator getParamEvaluator();

	public PropertiesContextScope getDefaultPropertiesScope();
	
	public PropertiesContext getPropertiesContext(PropertiesContextScope scope);
}
