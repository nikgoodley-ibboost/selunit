package org.selunit.webdriver.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.selunit.webdriver.PropertiesContextScope;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueParam {
	public boolean evaluate() default true;

	public PropertiesContextScope evaluationScope() default PropertiesContextScope.Suite;

}
