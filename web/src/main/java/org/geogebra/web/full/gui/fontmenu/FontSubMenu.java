package org.geogebra.web.full.gui.fontmenu;

import java.util.List;

import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.web.html5.gui.util.AriaMenuBar;
import org.geogebra.web.html5.gui.util.AriaMenuItem;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.dom.client.Style;

/**
 * Submenu for Font item in 3-dot menu of inline text
 *
 * @author Laszlo
 *
 */
public class FontSubMenu extends AriaMenuBar {

	public static final int VERTICAL_PADDING = 16;
	public static final String FALLBACK_FONT = "Arial";
	private final List<String> fonts;
	private final AppW app;

	/**
	 * @param app the application
	 * @param textController to format text.
	 */
	public FontSubMenu(AppW app, InlineTextController textController) {
		this.app = app;
		this.fonts = app.getVendorSettings().getTextToolFonts();
		addStyleName("fontSubMenu");
		setMaxHeight(app.getActiveEuclidianView().getHeight());
		createItems(textController);
		selectCurrent(textController);
	}

	private void selectCurrent(InlineTextController textController) {
		String font = textController.getFormat("font", FALLBACK_FONT);
		selectItem(fonts.indexOf(font));
	}

	private void createItems(InlineTextController textController) {
		for (String font : fonts) {
			AriaMenuItem item = new AriaMenuItem(font, false, new FontCommand(app, textController, font));
			item.addStyleName("no-image");
			addItem(item);
		}
	}

	private void setMaxHeight(int height) {
		getElement().getStyle().setProperty("maxHeight", height, Style.Unit.PX);
	}
}
