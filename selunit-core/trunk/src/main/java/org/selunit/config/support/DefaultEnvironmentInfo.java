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
package org.selunit.config.support;

import java.io.Serializable;

import org.selunit.config.EnvironmentInfo;


/**
 * Default bean for environment information.
 * 
 * @author mbok
 * 
 */
public class DefaultEnvironmentInfo implements EnvironmentInfo, Serializable {
	private static final long serialVersionUID = 7906343865464349478L;
	private String os, browser;

	public DefaultEnvironmentInfo() {
		super();
	}

	public DefaultEnvironmentInfo(String os, String browser) {
		this.os = os;
		this.browser = browser;
	}

	/**
	 * @return the os
	 */
	@Override
	public String getOs() {
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @return the browser
	 */
	@Override
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

}
