package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;

import com.google.gwt.user.client.ui.FlowPanel;

import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

public class H5PWrapper {

	public static final String DEFAULT_DATA = "http://tafel.dlb-dev01.alp-dlg.net/public/data/h5p";
	private final GeoEmbed geoEmbed;

	public H5PWrapper(App app, int embedId) {
		geoEmbed = new GeoEmbed(app.getKernel().getConstruction());
		geoEmbed.setEmbedId(embedId);
		geoEmbed.setAppName("h5p");
		geoEmbed.initDefaultPosition(app.getActiveEuclidianView());
	}

	public static void init(FlowPanel container) {
		init(container, DEFAULT_DATA);
	}

	public static void init(FlowPanel container, String url) {
		H5P h5P =
				new H5P(Js.cast(container.getElement()), url,
						getOptions(), getDisplayOptions());
	}


	private static JsPropertyMap<Object> getOptions() {
		JsPropertyMap<Object> options = JsPropertyMap.of();
		return options;
	}

	private static JsPropertyMap<Object> getDisplayOptions() {
		JsPropertyMap<Object> displayOptions = JsPropertyMap.of();
		displayOptions.set("frame", false);
		displayOptions.set("embed", false);
		displayOptions.set("download", false);
		return displayOptions;
	}

	public GeoEmbed getGeoEmbed() {
		return geoEmbed;
	}
}
