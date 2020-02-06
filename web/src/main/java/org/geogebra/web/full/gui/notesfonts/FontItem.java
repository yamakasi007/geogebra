package org.geogebra.web.full.gui.notesfonts;

/**
 * Class to store font and its fallback in case it is not available.
 */
class FontItem {
	String name;
	String fallback;

	public FontItem(String name, String fallback) {
		this.name = name;
		this.fallback = fallback;
	}
}
