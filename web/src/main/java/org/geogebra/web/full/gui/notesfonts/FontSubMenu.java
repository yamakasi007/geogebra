package org.geogebra.web.full.gui.notesfonts;

import java.util.List;

import org.geogebra.common.euclidian.draw.DrawInlineText;
import org.geogebra.web.html5.gui.util.AriaMenuBar;

import com.google.gwt.core.client.Scheduler;
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
	 * @param inlineText text to apply.
	 * @param fonts to display.
	 * @param height the height of the submenu panel.
	 */
	public FontSubMenu(DrawInlineText inlineText, List<String> fonts, int height) {
		this.fonts = fonts;
		addStyleName("mowScrollableSubmenu");
		setMaxHeight(height);
		createItems(inlineText);
	}

	private void createItems(DrawInlineText inlineText) {
		for (String font : fonts) {
			addItem(font, false, createFontCommand(inlineText, font));
		}
	}

	private Scheduler.ScheduledCommand createFontCommand(final DrawInlineText inlineText,
														 final String font) {
		return new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				inlineText.format("font", font);
			}
		};
	}

	private void setMaxHeight(int height) {
		getElement().getStyle().setProperty("maxHeight", height - VERTICAL_PADDING, Style.Unit.PX);
	}
}
