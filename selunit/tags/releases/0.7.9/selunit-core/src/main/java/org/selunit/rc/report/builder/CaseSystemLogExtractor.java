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
package org.selunit.rc.report.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CaseSystemLogExtractor {
	public static class CaseLogPair {
		private String caseFileName;
		private String log;
		private long startTime;
		private long endTime;

		private CaseLogPair(String caseFileName, String log, long startTime,
				long endTime) {
			this.caseFileName = caseFileName;
			this.log = log;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public String getCaseFileName() {
			return caseFileName;
		}

		public String getLog() {
			return log;
		}

		public long getStartTime() {
			return startTime;
		}

		public long getEndTime() {
			return endTime;
		}

	}

	private static Log log = LogFactory.getLog(CaseSystemLogExtractor.class);
	private Pattern CASE_LOG_PATTERN = Pattern.compile(
			"info: Start test case \"([^\"]+)\" at: (\\d+)\\s*" + ".*?"
					+ "info: Finished test case \"\\1\" at: (\\d+)\\s*",
			Pattern.MULTILINE | Pattern.DOTALL);

	public List<CaseLogPair> extractCaseLogs(String systemLog) {
		ArrayList<CaseLogPair> caseLogs = new ArrayList<CaseLogPair>();
		Matcher m = CASE_LOG_PATTERN.matcher(systemLog);

		while (m.find()) {
			CaseLogPair caseLog = new CaseLogPair(m.group(1), m.group(), Long
					.parseLong(m.group(2)), Long.parseLong(m.group(3)));
			caseLogs.add(caseLog);
			log.debug("Extracted case log: " + caseLog);
		}
		if (caseLogs.size() == 0) {
			log
					.warn("System logs don't match case pattern. Are the extended reporting components active?");
		}
		return caseLogs;
	}
}
