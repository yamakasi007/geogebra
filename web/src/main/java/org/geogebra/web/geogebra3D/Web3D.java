package org.geogebra.web.geogebra3D;

import org.geogebra.web.full.WebND;
import org.geogebra.web.full.gui.applet.AppletFactory;
import org.geogebra.web.full2d.AppletFactory2D;

public class Web3D extends WebND {
	@Override
	protected AppletFactory getAppletFactory() {
		return new AppletFactory3D();
	}
}
