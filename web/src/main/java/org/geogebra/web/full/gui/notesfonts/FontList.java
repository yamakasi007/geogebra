package org.geogebra.web.full.gui.notesfonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Class provides font list with fallbacks, if the given font is not available.
 *
 * @author Laszlo
 */
public class FontList {
	public static final String DEFAULT_FALLBACK_FONT = "Arial";
	private List<FontItem> fonts;

	FontList() {
		fonts = new ArrayList<>();
	}

	/**
	 * Adds a font with default fallback
	 * @param font to add.
	 */
	void add(String font) {
		add(font, DEFAULT_FALLBACK_FONT);
	}

	/**
	 * Adds a font with fallback.
	 * @param font the primary font to add.
	 * @param fallBack the fallback font.
	 */
	void add(String font, String fallBack) {
		fonts.add(new FontItem(font, fallBack));
	}

	String getFontName(int index) {
		return fonts.get(index).name;
	}

	String getFallbackFont(int index) {
		return fonts.get(index).fallback;
	}

	public int size() {
		return fonts.size();
	}
}
