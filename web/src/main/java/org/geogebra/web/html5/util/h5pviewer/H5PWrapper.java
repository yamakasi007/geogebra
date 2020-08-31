package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;

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

	public static void init(FlowPanel container, final GeoEmbed geoEmbed) {
		init(container, DEFAULT_DATA, geoEmbed);
	}

	public static void init(FlowPanel container, String url, final GeoEmbed geoEmbed) {
		H5P h5P = new H5P(Js.cast(container.getElement()), url,
						getOptions(), getDisplayOptions());
		h5P.then( p-> {
			Log.debug("H5P Content is loaded: " + container.getOffsetWidth()
			+ " x " + container.getOffsetHeight());
			geoEmbed.setContentHeight(container.getOffsetHeight());
			geoEmbed.setContentWidth(container.getOffsetWidth());
			return null;
					});

	}


	private static JsPropertyMap<Object> getOptions() {
		JsPropertyMap<Object> options = JsPropertyMap.of();
		options.set("frameJs", "../public/h5p/frame.bundle.js");
		options.set("frameCss", "../public/h5p/styles/h5p.css");
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
