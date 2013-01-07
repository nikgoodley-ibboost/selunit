package org.selunit.webdriver.commands.support;

import org.openqa.selenium.Point;
import org.selunit.webdriver.TestException;

/**
 * Command utilities.
 * 
 * @author mbok
 * 
 */
public class CommandUtils {
	/**
	 * Returns a point from string coordinates in the form "x,y".
	 * 
	 * @param coord
	 *            coordinates in the form "x,y"
	 * @return point for coordinates
	 * @throws TestException
	 *             in case of parsing errors
	 */
	public static final Point getPoint(String coordinates) throws TestException {
		String[] vs = coordinates.split(",", 2);
		if (vs.length == 2) {
			try {
				return new Point(Integer.parseInt(vs[0]),
						Integer.parseInt(vs[1]));
			} catch (NumberFormatException e) {
				throw new TestException("Invailid coordinates: " + coordinates,
						e);
			}
		}
		throw new TestException("Invailid coordinates: " + coordinates);
	}

}
