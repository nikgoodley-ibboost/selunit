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
