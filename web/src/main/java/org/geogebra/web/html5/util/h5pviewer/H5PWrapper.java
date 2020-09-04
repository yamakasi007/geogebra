package org.geogebra.web.html5.util.h5pviewer;

import org.geogebra.common.euclidian.EuclidianController;
import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.common.main.App;

import com.google.gwt.dom.client.Element;
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
	private final int embedId;
	public static final int SCALE = 3;
	private double initialHeight;
	private elemental2.dom.Element frame;
	private double initialRatio;
	private EuclidianController euclidianController;
	private boolean firstUpdate;

	/**
	 *
	 * @param app the application.
	 * @param embedId the id of the embed.
	 */
	public H5PWrapper(App app, int embedId) {
		geoEmbed = new GeoEmbed(app.getKernel().getConstruction());
		this.embedId = embedId;
		euclidianController = geoEmbed.getApp().getActiveEuclidianView().getEuclidianController();

		geoEmbed.setEmbedId(embedId);
		geoEmbed.setAppName("h5p");
		firstUpdate = true;
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
		Element element = container.getElement();
		if (element == null) {
			return;
		}

		H5P h5P = new H5P(Js.cast(element), url,
						getOptions(), getDisplayOptions());
		h5P.then(p -> {
			double w = container.getOffsetWidth();
			double h = container.getOffsetHeight() ;
			initialRatio = h / w;
			initialHeight = SCALE * initialRatio * w + BOTTOM_BAR;
			geoEmbed.setSize(SCALE * w, initialHeight);
			geoEmbed.initPosition(geoEmbed.getApp().getActiveEuclidianView());
			geoEmbed.updateRepaint();
			frame = Js.cast(element.getOwnerDocument()
					.getElementById("h5p-iframe-embed" + embedId));
			return null;
		});
	}

	private JsPropertyMap<Object> getOptions() {
		JsPropertyMap<Object> options = JsPropertyMap.of();
		options.set("id", "embed" + embedId);
		options.set("frameJs", "../public/h5p/frame.bundle.js");
		options.set("frameCss", "../public/h5p/styles/h5p.css");
		return options;
	}

	private static JsPropertyMap<Object> getDisplayOptions() {
		return JsPropertyMap.of();
	}

	/**
	 *
	 * @return embed associated with the H5P content.
	 */
	public GeoEmbed getGeoEmbed() {
		return geoEmbed;
	}

	/**
	 * Updates embed size keeping aspect ratio
	 *
	 * @param width of the resized embed
	 */
	public void update(int width) {
		ensureStylebarFirstPosition();
		double h = width * initialRatio + BOTTOM_BAR;
		double ratioY = 1 / (initialHeight / h);

		frame.parentElement.setAttribute("style", scale(ratioY));
	}

	protected void ensureStylebarFirstPosition() {
		if (firstUpdate) {
			euclidianController.showDynamicStylebar();
			firstUpdate = false;
		}
	}

	private String scale(double ratio) {
		return "transform-origin: 0 0;"
				+ "transform: scale(1, "
				+ ratio
				+ ");";
	}
}