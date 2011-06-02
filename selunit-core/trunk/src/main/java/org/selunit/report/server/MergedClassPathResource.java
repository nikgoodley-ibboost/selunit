package org.selunit.report.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openqa.jetty.util.IO;
import org.openqa.selenium.browserlaunchers.LauncherUtils;
import org.openqa.selenium.server.ClassPathResource;

public class MergedClassPathResource extends ClassPathResource {
	private static final long serialVersionUID = 2341357140046726744L;
	private ByteArrayOutputStream mergedContent = new ByteArrayOutputStream();

	public MergedClassPathResource(String originalResourceClassPath,
			String attachResourceClassPath) {
		super(originalResourceClassPath);
		try {
			IO.copy(super.getInputStream(), mergedContent);
			IO.copy(LauncherUtils
					.getSeleniumResourceAsStream(attachResourceClassPath),
					mergedContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(mergedContent.toByteArray());
	}

	@Override
	public long length() {
		return mergedContent.size();
	}

	@Override
	public boolean exists() {
		return true;
	}
}