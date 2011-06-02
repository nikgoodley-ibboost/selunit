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
			if (command.length() > 70) {
				command = StringUtils.abbreviate(command, 70);
			}
			if (selector.length() > 70) {
				selector = StringUtils.abbreviate(selector, 70);
			}
			if (value.length() > 70) {
				value = StringUtils.abbreviate(value, 70);
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
