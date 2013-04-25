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
package org.selunit.report.support;

import java.io.Serializable;

import org.selunit.report.TestReportLog;


public class DefaultResultLog implements TestReportLog, Serializable {
	private static final long serialVersionUID = 7881617071655067541L;
	private String htmlSummary;
	private String systemLog;

	public DefaultResultLog() {
		super();
	}

	public DefaultResultLog(String htmlSummary, String systemLog) {
		setHtmlSummary(htmlSummary);
		setSystemLog(systemLog);
	}

	@Override
	public String getHtmlSummary() {
		return htmlSummary;
	}

	public void setHtmlSummary(String htmlSummary) {
		this.htmlSummary = htmlSummary;
	}

	@Override
	public String getSystemLog() {
		return systemLog;
	}

	public void setSystemLog(String systemLog) {
		this.systemLog = systemLog;
	}

	@Override
	public String toString() {
		return "html-summary=#" + getHtmlSummary().length() + "; system-logs=#"
				+ getSystemLog().length() + ";";
	}
}
