package org.selunit.report.output;

import org.selunit.report.TestSuiteReport;

public interface OutputProcessor<I extends TestSuiteReport> {
	public void processOutput(I suite)
			throws OutputProcessException;
}
