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
package org.selunit.maven.docstubs;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.selunit.maven.config.SeleniumPropertiesConfig;

/**
 * Configuration bean to define Selenium properties used in a test job
 * execution.
 * 
 * @goal SeleniumPropertiesConfig
 */
public class SeleniumPropertiesConfig4Doc extends SeleniumPropertiesConfig
		implements Mojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Empty implementation, because used only for documentation creation
	}

	@Override
	public void setLog(Log log) {
		// Empty implementation, because used only for documentation creation
	}

	@Override
	public Log getLog() {
		// Empty implementation, because used only for documentation creation
		return null;
	}
}
