package org.geogebra.web.full.gui.notesfonts;

import java.util.List;

import org.geogebra.web.html5.gui.util.AriaMenuBar;

import com.google.gwt.dom.client.Style;

/**
 * Submenu for Font item in 3-dot menu of inline text
 *
 * @author Laszlo
 *
 */
public class FontSubMenu extends AriaMenuBar {

	public static final int VERTICAL_PADDING = 16;
	private final List<String> fonts;

	/**
	 * @param fonts to display.
	 * @param height the height of the submenu panel.
	 */
	public FontSubMenu(List<String> fonts, int height) {
		this.fonts = fonts;
		addStyleName("mowScrollableSubmenu");
		setMaxHeight(height);
		createItems();
	}

	private void createItems() {
		for (String font : fonts) {
			addItem(font, false, null);
		}
	}

	private void setMaxHeight(int height) {
		getElement().getStyle().setProperty("maxHeight", height - VERTICAL_PADDING, Style.Unit.PX);
	}
}
