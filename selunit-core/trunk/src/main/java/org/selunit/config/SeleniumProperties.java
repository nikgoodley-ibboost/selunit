/*******************************************************************************
 * Copyright 2011 selunit.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.selunit.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Capabilities;

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

	/**
	 * Additional browser capabilities as bridge to WebDriver's
	 * {@link Capabilities}.
	 * 
	 * @return additional browser capabilities
	 */
	public Map<String, ?> getBrowserCapabilities();

	public Properties getAsProperties();

}
