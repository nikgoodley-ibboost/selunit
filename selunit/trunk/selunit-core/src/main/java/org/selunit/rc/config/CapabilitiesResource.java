package org.selunit.rc.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.openqa.jetty.util.Resource;
import org.openqa.selenium.Capabilities;

/**
 * Provides current {@link Capabilities} as JS file where the capabilities are
 * exported as JSON object to the JS variable "Selenium.capabilities".
 * 
 * @author mbok
 * 
 */
public class CapabilitiesResource extends Resource {

	private static final long serialVersionUID = 4695058408512985238L;
	private byte[] asJsonBytes;

	/**
	 * Creates new resource.
	 * 
	 * @param capabilities
	 *            current capabilities.
	 */
	public CapabilitiesResource(Capabilities capabilities) {
		if (capabilities != null) {
			asJsonBytes = ("Selenium.capabilities=" + new JSONObject(
					capabilities.asMap()).toString()).getBytes();
		} else {
			asJsonBytes = "Selenium.capabilities={}".getBytes();
		}
	}

	@Override
	public void release() {
		// NOP
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public long lastModified() {
		return System.currentTimeMillis() + 1000l * 3600l * 24l * 365l;
	}

	@Override
	public long length() {
		return asJsonBytes.length;
	}

	@Override
	public URL getURL() {
		return null;
	}

	@Override
	public File getFile() throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return "capabilities.js";
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(asJsonBytes);
	}

	@Override
	public OutputStream getOutputStream() throws IOException, SecurityException {
		return null;
	}

	@Override
	public boolean delete() throws SecurityException {
		return false;
	}

	@Override
	public boolean renameTo(Resource dest) throws SecurityException {
		return false;
	}

	@Override
	public String[] list() {
		return new String[0];
	}

	@Override
	public Resource addPath(String path) throws IOException,
			MalformedURLException {
		return null;
	}

}
