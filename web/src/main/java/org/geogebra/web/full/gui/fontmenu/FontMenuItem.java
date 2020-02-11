package org.geogebra.web.full.gui.fontmenu;

import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.web.html5.gui.util.AriaMenuItem;
import org.geogebra.web.html5.main.AppW;

public class FontMenuItem extends AriaMenuItem {
	public FontMenuItem(AppW app, InlineTextController textController) {
		super("Font", false,
				new FontSubMenu(app, textController));
		addStyleName("no-image");
	}
}
