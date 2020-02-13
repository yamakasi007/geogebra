package org.geogebra.web.full.gui.fontmenu;

import java.util.List;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.web.html5.gui.util.AriaMenuBar;
import org.geogebra.web.html5.gui.util.AriaMenuItem;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

/**
 * Submenu for Font item in 3-dot menu of inline text
 *
 * @author Laszlo
 *
 */
public class FontSubMenu extends AriaMenuBar implements ResizeHandler {

	public static final int VERTICAL_PADDING = 32;
	public static final String FALLBACK_FONT = "Arial";
	public static final int FONT_SIZE = 32;
	private final List<String> fonts;
	private final AppW app;
	private final EuclidianView view;
	private Integer topWhenOpened = null;

	/**
	 * @param app the application
	 * @param textController to format text.
	 */
	public FontSubMenu(AppW app, InlineTextController textController) {
		this.app = app;
		this.fonts = app.getVendorSettings().getTextToolFonts();
		addStyleName("fontSubMenu");
		view = app.getActiveEuclidianView();
		createItems(textController);
		selectCurrent(textController);
		Window.addResizeHandler(this);
	}

	@Override
	public void onResize(ResizeEvent event) {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				resize();
			}
		});
	}

	private void resize() {
		int maxHeight = Window.getClientHeight();
		setMaxHeight(maxHeight);
	}

	private void setMaxHeight(int maxHeight) {
		getElement().getStyle().setProperty("maxHeight", maxHeight, Style.Unit.PX);
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
