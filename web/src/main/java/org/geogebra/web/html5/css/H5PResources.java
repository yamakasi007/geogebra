package org.geogebra.web.html5.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface H5PResources extends ClientBundle {

	H5PResources INSTANCE = GWT.create(H5PResources.class);
	
	@Source("org/geogebra/web/resources/js/h5pviewer/main.bundle.js")
	TextResource h5pmainJs();
}
