package org.selunit.webdriver.executor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.selunit.webdriver.TestException;

/**
 * Factory to return a locator {@link By} following the SeIDE syntax convention.
 * 
 * @author mbok
 * 
 */
public abstract class LocatorFactory {
	/**
	 * @see /core/scripts/htmlutils.js#parse_locator(locator)
	 */
	private static Pattern LOCATOR_TYPE_MATCHER = Pattern
			.compile("^([a-zA-Z]+)\\s*=(.+)");

	/**
	 * Converts an IDE locator to the appropriated {@link By} locator. Supported
	 * locator types are: xpath, css, id, name and link. A locator without a
	 * type notation with leading "//" characters is interpreted as a
	 * {@link ByXPath} locator. Inspired by
	 * IDE/content/formats/webdriver.js#SeleniumWebDriverAdaptor
	 * .prototype._elementLocator(sel1Locator).
	 * 
	 * @param locator
	 *            in the IDE syntax
	 * @return the appropriated {@link By} locator
	 * @throws TestException
	 *             in cases when locator isn't supported
	 */
	public static By getLocator(String locator) throws TestException {
		locator = locator.trim();
		Matcher m = LOCATOR_TYPE_MATCHER.matcher(locator);
		if (m.matches()) {
			String type = m.group(1).toLowerCase();
			locator = m.group(2);
			if ("xpath".equals(type)) {
				return By.xpath(locator);
			} else if ("css".equals(type)) {
				return By.cssSelector(locator);
			} else if ("id".equals(type)) {
				return By.id(locator);
			} else if ("name".equals(type)) {
				return By.name(locator);
			} else if ("link".equals(type)) {
				return By.linkText(locator);
			}
		} else if (locator.startsWith("//")) {
			return By.xpath(locator);
		}
		throw new TestException("Locator is not supported: " + locator);
	}
}
