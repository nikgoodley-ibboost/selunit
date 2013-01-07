package org.selunit.webdriver.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.PropertiesContextScope;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.ParamEvaluator;
import org.selunit.webdriver.PropertiesContext;
import org.selunit.webdriver.TestCase;

public class DefaultExecutionContext implements ExecutionContext,
		PropertiesContext {
	private Log log = LogFactory.getLog(getClass());
	private Command currentCommand, nextCommand;
	private WebDriver webDriver;
	private Capabilities capabilities;
	private String baseUrl;
	private ParamEvaluator paramEvaluator;
	private Map<String, Object> properties = new HashMap<String, Object>();
	private PropertiesContextScope defaultPropertiesScope = PropertiesContextScope.Suite;

	public DefaultExecutionContext() {
		super();
	}

	public DefaultExecutionContext(Command currentCommand, Command nextCommand,
			WebDriver webDriver, Capabilities capabilities, String baseUrl) {
		this.currentCommand = currentCommand;
		this.nextCommand = nextCommand;
		this.webDriver = webDriver;
		this.capabilities = capabilities;
		this.baseUrl = baseUrl;
	}

	@Override
	public Command getCurrentCommand() {
		return currentCommand;
	}

	@Override
	public Command getNextCommand() {
		return nextCommand;
	}

	@Override
	public void setNextCommand(Command next) {
		this.nextCommand = next;
	}

	@Override
	public WebDriver getWebDriver() {
		return webDriver;
	}

	@Override
	public Capabilities getCapabilities() {
		return capabilities;
	}

	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setCurrentCommand(Command currentCommand) {
		this.currentCommand = currentCommand;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public ParamEvaluator getParamEvaluator() {
		return paramEvaluator;
	}

	public void setParamEvaluator(ParamEvaluator paramEvaluator) {
		this.paramEvaluator = paramEvaluator;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public PropertiesContextScope getDefaultPropertiesScope() {
		return defaultPropertiesScope;
	}

	public void setDefaultPropertiesScope(
			PropertiesContextScope defaultPropertiesScope) {
		this.defaultPropertiesScope = defaultPropertiesScope;
	}

	@Override
	public PropertiesContext getPropertiesContext(PropertiesContextScope scope) {
		TestCase tc = null;
		if (scope == PropertiesContextScope.Case
				|| scope == PropertiesContextScope.Suite) {
			if (getCurrentCommand() != null
					&& getCurrentCommand().getTestCase() != null) {
				tc = getCurrentCommand().getTestCase();
				if (scope == PropertiesContextScope.Case) {
					return tc;
				}
			}
		}
		if (scope == PropertiesContextScope.Suite && tc != null
				&& tc.getSuite() != null) {
			return tc.getSuite();
		}
		log.warn("Can't resolve properties context scope " + scope
				+ " from command: " + getCurrentCommand()
				+ ". A singleton context is returned!");
		return this;
	}
}
