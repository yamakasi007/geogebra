package org.geogebra.web.full.gui.notesfonts;

import org.geogebra.common.main.App;
import org.geogebra.web.html5.gui.util.AriaMenuItem;

/**
 * Provides font entry for inline text 3-dot menu
 *
 * @author Laszlo
 */
public class FontMenu {
	private final AriaMenuItem item;

	/**
	 * Constructor
	 *
	 * @param app The application.
	 */
	public FontMenu(App app) {
		item = new AriaMenuItem("Font", false, createSubMenu(app));
		item.addStyleName("mowTextOnlyMenuItem");
	}

	private FontSubMenu createSubMenu(App app) {
		return new FontSubMenu(app.getActiveEuclidianView().getHeight(),
				app.isMebis() ? createMebisFonts() : createNotesFonts());
	}

	private FontList createMebisFonts() {
		FontList list = new FontList();
		list.add("Arial");
		list.add("Calibri");
		list.add("Century Gothic");
		list.add("Comic Sans");
		list.add("Courier");
		list.add("Georgia");
		list.add("Open dyslexic mit Fibel a", "Arial");
		list.add("Palatino", "Arial");
		list.add("Qicksand");
		list.add("Roboto");
		list.add("Schulbuch Bayern");
		list.add("SF Mono");
		list.add("SF Pro");
		list.add("Times");
		list.add("Titilium Web");
		list.add("Trebuchet");
		list.add("Verdana");

		return list;
	}

	private FontList createNotesFonts() {
		FontList list = new FontList();
		list.add("Arial");
		list.add("Calibri");
		list.add("Century Gothic");
		list.add("Comic Sans");
		list.add("Courier");
		list.add("Georgia");
		list.add("Roboto");
		list.add("SF Mono");
		list.add("SF Pro");
		list.add("Times");
		list.add("Trebuchet");
		list.add("Verdana");
		return list;
	}

	public AriaMenuItem getItem() {
		return item;
	}
}
