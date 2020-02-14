package org.geogebra.web.full.gui.fontmenu;

import java.util.List;

import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.web.html5.gui.laf.FontFamily;
import org.geogebra.web.html5.gui.util.AriaMenuBar;
import org.geogebra.web.html5.gui.util.AriaMenuItem;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.user.client.ui.Widget;

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
	private final List<FontFamily> fonts;
	private final AppW app;

	/**
	 * @param app the application
	 * @param textController to format text.
	 */
	public FontSubMenu(AppW app, InlineTextController textController) {
		this.app = app;
		this.fonts = app.getVendorSettings().getTextToolFonts();
		createItems(textController);
		selectCurrent(textController);
	}

	private void selectCurrent(InlineTextController textController) {
		String font = textController.getFormat("font", FALLBACK_FONT);
		for (FontFamily family : fonts) {
			if (font.equals(family.cssName())) {
				selectItem(fonts.indexOf(family));
				return;
			}
		}
	}

	private void createItems(InlineTextController textController) {
		for (FontFamily font : fonts) {
			AriaMenuItem item = new AriaMenuItem(font.displayName(),
					false,
					new FontCommand(app, textController, font.cssName()));
			item.addStyleName("no-image");
			addItem(item);
		}
	}

	@Override
	public void stylePopup(Widget widget) {
		widget.addStyleName("fontSubMenu");
	}
}
