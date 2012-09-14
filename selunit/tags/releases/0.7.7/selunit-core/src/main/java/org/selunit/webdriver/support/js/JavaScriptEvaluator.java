package org.selunit.webdriver.support.js;

import java.util.regex.Pattern;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.selunit.webdriver.EvaluationException;
import org.selunit.webdriver.ParamEvaluator;
import org.selunit.webdriver.PropertiesContext;

public class JavaScriptEvaluator extends ParamEvaluator {
	private static final Pattern jsSplitPattern = Pattern
			.compile("javascript\\s*\\{");
	private ParamEvaluator priorEvaluator;

	@Override
	public String evaluate(PropertiesContext propertiesContext,
			String paramValue) throws EvaluationException {
		String[] jsBlocks = jsSplitPattern.split(paramValue);
		if (jsBlocks.length > 1) {
			StringBuilder evalText = new StringBuilder(evaluateByPrior(
					propertiesContext, jsBlocks[0]));
			for (int i = 1; i < jsBlocks.length; i++) {
				String body = jsBlocks[i];
				int openStart = -1, endStart = -1;
				String js = null, rightText = null;
				do {
					openStart = body.indexOf("{", openStart + 1);
					endStart = body.indexOf("}", endStart + 1);
					if (openStart < 0 && endStart >= 0) {
						js = body.substring(0, endStart);
						if (endStart + 1 < body.length()) {
							rightText = body.substring(endStart + 1);
						}
						break;
					}
				} while (openStart >= 0 && endStart >= 0
						&& openStart + 1 < body.length()
						&& endStart + 1 < body.length());
				if (js == null) {
					throw new EvaluationException(
							"Right javascript bracket not found in: javascript{"
									+ body);
				} else {
					evalText.append(eval(propertiesContext, js));
					if (rightText != null) {
						evalText.append(evaluateByPrior(propertiesContext,
								rightText));
					}
				}
			}
			return evalText.toString();
		} else {
			return evaluateByPrior(propertiesContext, paramValue);
		}
	}

	public static String eval(PropertiesContext propertiesContext, String js) {
		Context cx = ContextFactory.getGlobal().enterContext();
		Scriptable scope = cx.initStandardObjects();
		try {
			ScriptableObject.defineProperty(scope, "storedVars", NativeJavaMap
					.wrap(scope, propertiesContext.getProperties()),
					ScriptableObject.EMPTY);
			Object result = cx.evaluateString(scope, js, null, 1, null);
			return Context.toString(result);
		} catch (Exception e) {
			throw new EvaluationException(
					"Error evaluating javascript expression: " + e.getMessage(),
					e);
		}
	}

	private String evaluateByPrior(PropertiesContext propertiesContext,
			String paramValue) {
		if (getPriorEvaluator() != null) {
			return getPriorEvaluator().evaluate(propertiesContext, paramValue);
		} else {
			return paramValue;
		}
	}

	public ParamEvaluator getPriorEvaluator() {
		return priorEvaluator;
	}

	public void setPriorEvaluator(ParamEvaluator priorEvaluator) {
		this.priorEvaluator = priorEvaluator;
	}

}
