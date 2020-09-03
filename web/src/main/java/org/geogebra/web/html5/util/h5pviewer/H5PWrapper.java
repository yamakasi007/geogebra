package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;

import com.google.gwt.user.client.Element;
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
	private Widget container;
	private double initialHeight;
	private double initialWidth;
	private elemental2.dom.Element frame;

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
	 */
	public void render(Widget container) {
		render(container, DEFAULT_DATA);
	}

	/**
	 * Renders H5P content to the given widget widget
	 *
	 * @param container widget to render to.
	 * @param url to the H5P resource.
	 */
	public void render(Widget container, String url) {
		this.container = container;
		Element element = container.getElement();
		H5P h5P = new H5P(Js.cast(element), url,
						getOptions(), getDisplayOptions());
		h5P.then(p -> {
			initialWidth = container.getOffsetWidth();
			double h = container.getOffsetHeight() ;
			double ratio = h / initialWidth;
			initialHeight = SCALE * ratio * initialWidth + BOTTOM_BAR;
			geoEmbed.setSize(SCALE * initialWidth, initialHeight);
			geoEmbed.initPosition(geoEmbed.getApp().getActiveEuclidianView());
			geoEmbed.updateRepaint();
//			container.getParent().getElement().getStyle().clearHeight();
			frame = Js.cast(element.getOwnerDocument().
					getElementById("h5p-iframe-example"));
			return null;
		});

	}

	private JsPropertyMap<Object> getOptions() {
		JsPropertyMap<Object> options = JsPropertyMap.of();
		options.set("id", "example");
		options.set("frameJs", "../public/h5p/frame.bundle.js");
		options.set("frameCss", "../public/h5p/styles/h5p.css");
		return options;
	}

	private static JsPropertyMap<Object> getDisplayOptions() {
		return JsPropertyMap.of();
	}

	public GeoEmbed getGeoEmbed() {
		return geoEmbed;
	}

	public void update(int width, int height) {
		double ratio = 1 / (initialWidth / width);
		Log.debug("embed " + dim(width, height) + " container "
			+ dim(container.getOffsetWidth(), initialHeight)
		 + "ratio " + ratio);
		frame.setAttribute("style", "transform-origin: 0 0;" +
				"transform: scale(" + ratio +")");
	}

	private String dim(double width, double height) {
		return width + " x " + height;
	}
}
