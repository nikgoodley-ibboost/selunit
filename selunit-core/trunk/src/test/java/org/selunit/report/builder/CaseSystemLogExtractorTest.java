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
package org.selunit.report.builder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.selunit.report.builder.CaseSystemLogExtractor;
import org.selunit.report.builder.CaseSystemLogExtractor.CaseLogPair;


public class CaseSystemLogExtractorTest {
	private String log;

	@Before
	public void readLog() throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		InputStream logInput = getClass().getResourceAsStream(
				"/test-selenium-logs.log");
		byte[] b = new byte[4096];
		int len;
		while ((len = logInput.read(b)) >= 0) {
			if (len > 0) {
				bo.write(b, 0, len);
			}
		}
		logInput.close();
		log = new String(bo.toByteArray(), "UTF-8");
	}

	@Test
	public void testExtractor() throws Exception {
		CaseSystemLogExtractor extractor = new CaseSystemLogExtractor();
		List<CaseLogPair> caseLogs = extractor.extractCaseLogs(log);
		Assert.assertEquals(4, caseLogs.size());
		Assert.assertEquals(
				"/selenium-server/tests/testcases/GoogleSearch3.html", caseLogs
						.get(0).getCaseFileName());
		Assert.assertEquals(
				"/selenium-server/tests/testcases/GoogleSearch1.html", caseLogs
						.get(1).getCaseFileName());
		Assert.assertEquals(
				"/selenium-server/tests/testcases/GoogleSearch2.html", caseLogs
						.get(2).getCaseFileName());
		Assert.assertEquals(
				"/selenium-server/tests/testcases/GoogleSearch3.html", caseLogs
						.get(3).getCaseFileName());
		Assert.assertEquals(1282984547609l, caseLogs.get(3).getStartTime());
		Assert.assertEquals(1282984547898l, caseLogs.get(3).getEndTime());

		// Test completeness of case logs
		StringBuilder aggregatedLogs = new StringBuilder();
		aggregatedLogs.append(caseLogs.get(0).getLog());
		aggregatedLogs.append(caseLogs.get(1).getLog());
		aggregatedLogs.append(caseLogs.get(2).getLog());
		aggregatedLogs.append(caseLogs.get(3).getLog());
		Assert.assertEquals(
				"Aggregated case logs don't conform to original logs", log
						.trim(), aggregatedLogs.toString().trim());
	}
}
