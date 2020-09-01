package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;

import com.google.gwt.user.client.ui.Widget;

import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * Renders H5P content.
 *
 * @author laszlo
 */
public class H5PWrapper {

	public static final String DEFAULT_DATA = "http://tafel.dlb-dev01.alp-dlg.net/public/data/h5p";
	public static final int BOTTOM_BAR = 48;
	private final GeoEmbed geoEmbed;
	public static final int SCALE = 3;

	/**
	 *
	 * @param app the application.
	 * @param embedId the id of the embed.
	 */
	public H5PWrapper(App app, int embedId) {
		geoEmbed = new GeoEmbed(app.getKernel().getConstruction());
		geoEmbed.setEmbedId(embedId);
		geoEmbed.setAppName("h5p");
	}

	/**
	 * Renders a predefined, example H5P content to the given container widget.
	 *
	 * @param container widget to render to.
	 * @param geoEmbed associated with the example content.
	 */
	public static void render(Widget container, final GeoEmbed geoEmbed) {
		render(container, DEFAULT_DATA, geoEmbed);
	}

	/**
	 * Renders H5P content to the given widget widget
	 *
	 * @param container widget to render to.
	 * @param url to the H5P resource.
	 * @param geoEmbed associated with the given H5P content.
	 */
	public static void render(Widget container, String url, final GeoEmbed geoEmbed) {
		H5P h5P = new H5P(Js.cast(container.getElement()), url,
						getOptions(), getDisplayOptions());
		h5P.then(p -> {
			double w = container.getOffsetWidth() ;
			double h = container.getOffsetHeight() ;
			double ratio = h / w;
			geoEmbed.setSize(SCALE * w, SCALE * ratio * w + BOTTOM_BAR);
			geoEmbed.initPosition(geoEmbed.getApp().getActiveEuclidianView());
			geoEmbed.updateRepaint();
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
//		displayOptions.set("frame", true);
//		displayOptions.set("embed", false);
//		displayOptions.set("download", false);
		return displayOptions;
	}

	public GeoEmbed getGeoEmbed() {
		return geoEmbed;
	}
}
