package org.geogebra.web.full.gui.menubar.action;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.full.gui.menubar.DefaultMenuAction;
import org.geogebra.web.full.main.AppWFull;

public class SwitchCalculatorAction extends DefaultMenuAction<Void> {
	@Override
	public void execute(Void item, AppWFull app) {
		Log.debug("SWITCH CALC CLICKED");
	}
}
