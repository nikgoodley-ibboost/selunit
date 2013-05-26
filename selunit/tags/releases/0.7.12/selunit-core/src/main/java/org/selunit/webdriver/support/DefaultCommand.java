package org.selunit.webdriver.support;

import java.util.HashMap;
import java.util.Map;

import org.selunit.webdriver.Command;
import org.selunit.webdriver.TestCase;

public class DefaultCommand implements Command {
	private String name;
	private Map<String, String> params;
	private TestCase testCase;
	private int commandIndex = -1;

	public DefaultCommand() {
		super();
	}

	public DefaultCommand(String name, Map<String, String> params,
			TestCase testCase, int commandIndex) {
		super();
		this.name = name;
		this.params = params != null ? params : new HashMap<String, String>();
		this.testCase = testCase;
		this.commandIndex = commandIndex;
	}

	public DefaultCommand(String name, String target, String value,
			TestCase testCase, int commandIndex) {
		this(name, null, testCase, commandIndex);
		getParams().put(Command.PARAM_TARGET, target);
		getParams().put(Command.PARAM_VALUE, value);
	}

	public DefaultCommand(String name, String target, String value) {
		this(name, target, value, null, -1);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	@Override
	public int getCommandIndex() {
		return commandIndex;
	}

	public void setCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
	}

	@Override
	public String toString() {
		return getName() + ": " + getParams();
	}

}
