package org.selunit.webdriver;

import java.util.Map;

public interface Command extends CommandContext {
	public static final String PARAM_TARGET = "target";
	public static final String PARAM_VALUE = "value";

	public String getName();

	public Map<String, String> getParams();
}
