package org.geogebra.web.html5.util;

import com.google.gwt.user.client.ui.UIObject;

public class TestHarness {

	public static void setAttr(UIObject widget, String value) {
		if (widget != null) {
			widget.getElement().setAttribute("data-test", value);
		}
	}

}
