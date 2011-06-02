package org.selunit.maven;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.selunit.maven.JUnitReportConverter;

/**
 * Test for {@link JUnitReportConverter}.
 * 
 * @author mbok
 * 
 */
public class JUnitReportConverterTest {
	@Test
	public void testConverting() throws Exception {
		JUnitReportConverter converter = new JUnitReportConverter();
		converter.convert(new FileInputStream(new File(
				"src/test/resources/selunitReport.xml")), System.out);
	}
}
