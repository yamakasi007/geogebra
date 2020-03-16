package org.geogebra.web.html5.euclidian;

import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.web.html5.gui.util.MathKeyboardListener;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface HasMathKeyboardListener {

	/**
	 * @return keyboard listener
	 */
	MathKeyboardListener getKeyboardListener();
}
