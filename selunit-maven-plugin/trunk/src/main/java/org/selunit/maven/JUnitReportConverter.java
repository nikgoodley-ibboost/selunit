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
package org.selunit.maven;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Converts Selunit reports to JUnit report format by XSLT "selunit2junit.xsl".
 * 
 * @author mbok
 * 
 */
public class JUnitReportConverter {
	/**
	 * Max character length per text column
	 */
	public static final int MAX_TEXT_COL_LEN = 120;
	private TransformerFactory transFact = TransformerFactory.newInstance();
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private boolean convertHtmlLog2Text = false;

	public JUnitReportConverter() throws ParserConfigurationException {
		dbf.setValidating(false);
		dbf.setFeature(
				"http://apache.org/xml/features/nonvalidating/load-external-dtd",
				false);
	}

	public void convert(InputStream selunitReport, OutputStream junitReport)
			throws TransformerException, ParserConfigurationException,
			SAXException, IOException {
		Transformer transformer = transFact.newTransformer(new StreamSource(
				getClass().getResourceAsStream("/selunit2junit.xsl")));
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document selunitReportDom = builder.parse(selunitReport);
		if (isConvertHtmlLog2Text()) {
			NodeList htmlLogs = selunitReportDom
					.getElementsByTagName("html-summary");
			for (int i = 0; i < htmlLogs.getLength(); i++) {
				Node htmlSummary = htmlLogs.item(i);
				String plainText = getHtmlSummaryAsText(htmlSummary);
				htmlSummary.setTextContent(plainText);
			}
		}
		DOMSource selunitSource = new DOMSource(selunitReportDom);

		StreamResult junitOutput = new StreamResult(junitReport);

		transformer.transform(selunitSource, junitOutput);
	}

	public String getHtmlSummaryAsText(Node node) {
		StringBuilder plain = new StringBuilder();
		String html = node.getTextContent();
		org.jsoup.nodes.Document summary = Jsoup.parse(html);
		String title = summary.select("thead td").text().replace('\u00A0', ' ');

		ArrayList<String[]> plainTable = new ArrayList<String[]>();
		int cm = 0, sm = 0, vm = 0;
		for (Element row : summary.select("tbody tr")) {
			Elements cells = row.select("td");
			String command = cells.get(0).text().replace('\u00A0', ' ');
			String selector = cells.get(1).text().replace('\u00A0', ' ');
			String value = cells.get(2).text().replace('\u00A0', ' ');
			if (command.length() > MAX_TEXT_COL_LEN) {
				command = StringUtils.abbreviate(command, MAX_TEXT_COL_LEN);
			}
			if (selector.length() > MAX_TEXT_COL_LEN) {
				selector = StringUtils.abbreviate(selector, MAX_TEXT_COL_LEN);
			}
			if (value.length() > MAX_TEXT_COL_LEN) {
				value = StringUtils.abbreviate(value, MAX_TEXT_COL_LEN);
			}
			plainTable.add(new String[] { command, selector, value,
					row.hasClass("status_failed") ? "f" : "p" });
			cm = Math.max(cm, command.length());
			sm = Math.max(sm, selector.length());
			vm = Math.max(vm, value.length());
		}

		plain.append(title);
		plain.append("\n");
		plain.append(StringUtils.repeat("=",
				Math.max(cm + sm + vm + 9, title.length())));
		plain.append("\n");

		for (String[] row : plainTable) {
			if (row[3].equals("f")) {
				plain.append(">> ");
			} else {
				plain.append("   ");
			}
			plain.append(row[0]);
			plain.append(StringUtils.repeat(" ", cm - row[0].length()));
			plain.append(" | ");
			plain.append(row[1]);
			plain.append(StringUtils.repeat(" ", sm - row[1].length()));
			plain.append(" | ");
			plain.append(row[2]);
			plain.append("\n");
		}
		return plain.toString();
	}

	/**
	 * @return the convertHtmlLog2Text
	 */
	public boolean isConvertHtmlLog2Text() {
		return convertHtmlLog2Text;
	}

	/**
	 * @param convertHtmlLog2Text
	 *            the convertHtmlLog2Text to set
	 */
	public void setConvertHtmlLog2Text(boolean convertHtmlLog2Text) {
		this.convertHtmlLog2Text = convertHtmlLog2Text;
	}

}
