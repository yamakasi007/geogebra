package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;

public class H5PWrapper {

	private final GeoEmbed geoEmbed;

	public H5PWrapper(App app, int embedId) {
		geoEmbed = new GeoEmbed(app.getKernel().getConstruction());
		geoEmbed.setEmbedId(embedId);
		geoEmbed.setAppName("h5p");
		geoEmbed.initDefaultPosition(app.getActiveEuclidianView());
	}

	public GeoEmbed getGeoEmbed() {
		return geoEmbed;
	}
}
