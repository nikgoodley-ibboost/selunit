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
import java.util.HashMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReportEntityResolver implements EntityResolver {
	private static HashMap<String, String> dtdMap = new HashMap<String, String>();
	static {
		dtdMap.put("-//Selunit//DTD Selunit Report 1.0.0//EN",
				"selunit-report_1_0_0.dtd");
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		if (dtdMap.containsKey(publicId)) {
			return new InputSource(getClass().getResourceAsStream(
					"/DTD/" + dtdMap.get(publicId)));
		}
		return null;
	}

}
