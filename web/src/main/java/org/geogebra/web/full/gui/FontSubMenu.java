package org.geogebra.web.full.gui;

import org.geogebra.web.html5.gui.util.AriaMenuBar;

import com.google.gwt.dom.client.Style;

public class FontSubMenu extends AriaMenuBar {

	public static final int VERTICAL_PADDING = 16;

	FontSubMenu(int height) {
		addStyleName("mowScrollableSubmenu");
		setMaxHeight(height);
		createTestItems();
	}

	private void createTestItems() {
		for (int i=0;i < 20; i++) {
			addItem("Item " + i, false,null);
		}
	}

	private void setMaxHeight(int height) {
		getElement().getStyle().setProperty("maxHeight", height - VERTICAL_PADDING, Style.Unit.PX);
	}
}
