package org.selunit.report.output;

import java.io.IOException;
import java.io.OutputStream;

import org.selunit.report.TestSuiteReport;


public interface OutputStreamFactory<I extends TestSuiteReport> {
	public OutputStream createReportOutputStream(I suite)
			throws IOException;
}
