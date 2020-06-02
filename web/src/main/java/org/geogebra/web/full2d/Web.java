package org.geogebra.web.full2d;

import org.geogebra.web.full.WebND;
import org.geogebra.web.full.gui.applet.AppletFactory;

public class Web extends WebND {
	@Override
	protected AppletFactory getAppletFactory() {
		return new AppletFactory2D();
	}
}
