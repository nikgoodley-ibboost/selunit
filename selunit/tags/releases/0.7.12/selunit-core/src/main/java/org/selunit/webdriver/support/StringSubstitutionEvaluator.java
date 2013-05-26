package org.selunit.webdriver.support;

import org.apache.commons.exec.util.StringUtils;
import org.selunit.webdriver.ParamEvaluator;
import org.selunit.webdriver.PropertiesContext;

public class StringSubstitutionEvaluator extends ParamEvaluator {

	@Override
	public String evaluate(PropertiesContext propertiesContext,
			String paramValue) {
		return StringUtils.stringSubstitution(paramValue,
				getStoredVars(propertiesContext), true).toString();
	}

}
