package org.geogebra.web.full.gui;

import java.util.ArrayList;
import java.util.List;

import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.web.full.html5.GMenuBarMock;
import org.geogebra.web.full.html5.MenuFactory;
import org.geogebra.web.html5.main.AppW;

public class ContextMenuMock {
	private final AppW app;
	private final ContextMenuFactory factory;


	public ContextMenuMock(AppW app) {
		this.app = app;
		this.factory = new MenuFactory(app);
	}

	List<String> getMenuEntriesFor(ArrayList<GeoElement> geos) {
		ContextMenuGeoElementW contextMenu =
				new ContextMenuGeoElementW(app, geos, factory);
		contextMenu.addOtherItems();
		GMenuBarMock menu = (GMenuBarMock) contextMenu.getWrappedPopup().getPopupMenu();
		return menu.getTitles();
	}
}