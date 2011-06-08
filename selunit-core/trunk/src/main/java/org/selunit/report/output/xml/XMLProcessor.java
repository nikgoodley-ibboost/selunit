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
package org.selunit.report.output.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.selunit.report.TestSuiteReport;
import org.selunit.report.output.OutputProcessException;
import org.selunit.report.output.OutputProcessor;
import org.selunit.report.output.OutputStreamFactory;


public class XMLProcessor implements OutputProcessor<TestSuiteReport> {
	private List<XMLOutputHandler<TestSuiteReport>> handler = new ArrayList<XMLOutputHandler<TestSuiteReport>>(Collections
			.singletonList((XMLOutputHandler<TestSuiteReport>) new DefaultOutputHandler()));

	private OutputStreamFactory<TestSuiteReport> outputStreamFactory;

	@Override
	public void processOutput(TestSuiteReport suite)
			throws OutputProcessException {
		Document jdom = new Document();
		try {
			if (getHandler() != null) {
				for (XMLOutputHandler<TestSuiteReport> handler : getHandler()) {
					handler.handleOutput(suite, jdom);
				}
			}
			XMLOutputter serializer = new XMLOutputter();
			serializer.setFormat(Format.getPrettyFormat());
			OutputStream outputStream;
			outputStream = getOutputStreamFactory().createReportOutputStream(
					suite);
			serializer.output(jdom, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			throw new OutputProcessException(suite,
					"Error processing suite report output", e);
		}
	}

	public List<XMLOutputHandler<TestSuiteReport>> getHandler() {
		return handler;
	}

	public void setHandler(List<XMLOutputHandler<TestSuiteReport>> handler) {
		this.handler = handler;
	}

	public OutputStreamFactory<TestSuiteReport> getOutputStreamFactory() {
		return outputStreamFactory;
	}

	public void setOutputStreamFactory(
			OutputStreamFactory<TestSuiteReport> outputStreamFactory) {
		this.outputStreamFactory = outputStreamFactory;
	}

}
