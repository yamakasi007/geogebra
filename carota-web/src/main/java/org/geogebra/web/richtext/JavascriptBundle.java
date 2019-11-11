package org.geogebra.web.richtext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JavascriptBundle extends ClientBundle {

	JavascriptBundle INSTANCE = GWT.create(JavascriptBundle.class);

	@Source("org/geogebra/web/richtext/js/carota.min.js")
	TextResource carotaJs();
}
