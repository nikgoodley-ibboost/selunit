package org.selunit.webdriver;

import java.util.HashMap;
import java.util.Map;

public abstract class ParamEvaluator {

	public static final String PROPERTIES_KEY_STORED_VARS = "storedVars";

	public abstract String evaluate(PropertiesContext propertiesContext,
			String paramValue) throws EvaluationException;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getStoredVars(PropertiesContext propertiesContext) {
		Map<String, Object> contextProperties = propertiesContext
				.getProperties();
		if (!contextProperties.containsKey(PROPERTIES_KEY_STORED_VARS)) {
			contextProperties.put(PROPERTIES_KEY_STORED_VARS,
					new HashMap<String, Object>());
		}
		return (Map<String, Object>) contextProperties
				.get(PROPERTIES_KEY_STORED_VARS);
	}
}
