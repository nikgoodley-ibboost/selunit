package org.selunit.report.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.jetty.http.HttpContext;
import org.openqa.jetty.http.HttpException;
import org.openqa.jetty.http.HttpHandler;
import org.openqa.jetty.http.HttpRequest;
import org.openqa.jetty.http.HttpResponse;
import org.openqa.jetty.util.StringUtil;

public class ExtHTMLTestResultsHandler implements HttpHandler {
	private static final long serialVersionUID = -2474636616141216820L;
	private static Log log = LogFactory.getLog(ExtHTMLTestResultsHandler.class);
	private HttpContext context;
	private boolean started = false;
	private List<ExtHTMLTestResultsListener> listeners = new Vector<ExtHTMLTestResultsListener>();

	public void addListener(ExtHTMLTestResultsListener listener) {
		listeners.add(listener);
	}

	@Override
	public HttpContext getHttpContext() {
		return context;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void handle(String pathInContext, String pathParams,
			HttpRequest request, HttpResponse res) throws HttpException,
			IOException {
		if (!"/postExtResults".equals(pathInContext))
			return;
		request.setHandled(true);
		log.info("Received posted extended results");

		ExtHTMLTestResults results = new ExtHTMLTestResults();

		for (Iterator<ExtHTMLTestResultsListener> i = listeners.iterator(); i
				.hasNext();) {
			ExtHTMLTestResultsListener listener = i.next();
			listener.processResults(results);
			i.remove();
		}
		Writer writer = new OutputStreamWriter(res.getOutputStream(), StringUtil.__ISO_8859_1);
		writer.write("<html></html>");
		writer.flush();
	}

	@Override
	public void initialize(HttpContext context) {
		this.context = context;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void start() throws Exception {
		started = true;
	}

	@Override
	public void stop() throws InterruptedException {
		started = false;
	}
}
