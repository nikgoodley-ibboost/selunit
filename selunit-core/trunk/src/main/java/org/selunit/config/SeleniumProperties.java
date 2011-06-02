package org.selunit.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

public interface SeleniumProperties {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MapToProperty {
		String name();
	}

	@MapToProperty(name = "browser")
	public String getBrowserKey();

	@MapToProperty(name = "browser-url")
	public String getBrowserURL();

	@MapToProperty(name = "multi-window")
	public boolean isMultiWindow();

	@MapToProperty(name = "timeout")
	public int getTimeoutInSeconds();

	@MapToProperty(name = "port")
	public int getPort();

	@MapToProperty(name = "userExtensions")
	public String getUserExtensions();

	public Properties getAsProperties();

}
