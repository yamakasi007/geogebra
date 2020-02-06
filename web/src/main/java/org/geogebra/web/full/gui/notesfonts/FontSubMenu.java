package org.geogebra.web.full.gui.notesfonts;

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
	private final FontList fontList;

	/**
	 *
	 * @param height the height of the submenu panel.
	 * @param fontList to display.
	 */
	FontSubMenu(int height, FontList fontList) {
		this.fontList = fontList;
		addStyleName("mowScrollableSubmenu");
		setMaxHeight(height);
		createItems();
	}

	private void createItems() {
		for (int i = 0; i < fontList.size(); i++) {
			addItem(fontList.getFontName(i), false, null);
		}
	}

	private void setMaxHeight(int height) {
		getElement().getStyle().setProperty("maxHeight", height - VERTICAL_PADDING, Style.Unit.PX);
	}
}
