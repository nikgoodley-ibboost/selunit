package org.selunit.report.support;

import java.io.Serializable;

import org.selunit.report.TestReportLog;


public class DefaultResultLog implements TestReportLog, Serializable {
	private static final long serialVersionUID = 7881617071655067541L;
	private String htmlSummary;
	private String systemLog;

	public DefaultResultLog() {
		super();
	}

	public DefaultResultLog(String htmlSummary, String systemLog) {
		setHtmlSummary(htmlSummary);
		setSystemLog(systemLog);
	}

	@Override
	public String getHtmlSummary() {
		return htmlSummary;
	}

	public void setHtmlSummary(String htmlSummary) {
		this.htmlSummary = htmlSummary;
	}

	@Override
	public String getSystemLog() {
		return systemLog;
	}

	public void setSystemLog(String systemLog) {
		this.systemLog = systemLog;
	}

	@Override
	public String toString() {
		return "html-summary=#" + getHtmlSummary().length() + "; system-logs=#"
				+ getSystemLog().length() + ";";
	}
}
