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

	public static final int VERTICAL_PADDING = 32;
	public static final String FALLBACK_FONT = "Arial";
	public static final int FONT_SIZE = 32;
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
		makeResponsive(app.getActiveEuclidianView().getHeight());
		createItems(textController);
		selectCurrent(textController);
	}

	private void makeResponsive(int maxHeight) {
		double itemsHeightViewPort = (getItemHeight() / maxHeight) * 100;
		setHeight(itemsHeightViewPort + "vh");
		getElement().getStyle().setProperty("maxHeight", getItemHeight(), Style.Unit.PX);
	}

	private double getItemHeight() {
		return fonts.size() * FONT_SIZE + VERTICAL_PADDING;
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

}
