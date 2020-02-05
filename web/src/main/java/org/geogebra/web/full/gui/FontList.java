package org.geogebra.web.full.gui;

import java.util.ArrayList;
import java.util.List;

public class FontList {
	public static final String DEFAULT_FALLBACK_FONT = "Arial";

	private static class FontItem {
		String name;
		String fallback;

		public FontItem(String name, String fallback) {
			this.name = name;
			this.fallback = fallback;
		}
	};

	private List<FontItem> fonts;

	FontList() {
		fonts = new ArrayList<>();
	}

	void add(String font) {
		add(font, DEFAULT_FALLBACK_FONT);
	}
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
