package org.selunit.report.output.xml;

import java.io.IOException;

import org.jdom.Document;
import org.selunit.report.TestSuiteReport;


public interface XMLOutputHandler<I extends TestSuiteReport> {
	public void handleOutput(I suite, Document jdom) throws IOException;
}
