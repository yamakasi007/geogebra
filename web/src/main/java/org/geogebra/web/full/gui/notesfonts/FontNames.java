package org.geogebra.web.full.gui.notesfonts;

import java.util.Arrays;
import java.util.List;

/**
 * Font name list constants for GGB+MOW
 *
 * You can define callbacks by adding "font, font2,"
 * comma-separated strings.
 *
 * @author Laszlo
 */
public class FontNames {

	/**
	 * @return font list for GGB notes
	 */
	public static List<String> getNotes() {
		return Arrays.asList("Arial",
				"Calibri",
				"Century Gothic",
				"Comic Sans",
				"Courier",
				"Georgia",
				"Roboto",
				"SF Mono",
				"SF Pro",
				"Times",
				"Trebuchet",
				"Verdana");
	}

	/**
	 * @return font list for MOW
	 */
	public static List<String> getMow() {
		return Arrays.asList("Arial",
				"Calibri",
				"Century Gothic",
				"Comic Sans",
				"Courier",
				"Georgia",
				"Open dyslexic mit Fibel a",
				"Palatino",
				"Qicksand",
				"Roboto",
				"Schulbuch Bayern",
				"SF Mono",
				"SF Pro",
				"Times",
				"Titilium Web",
				"Trebuchet",
				"Verdana");
	}
}
