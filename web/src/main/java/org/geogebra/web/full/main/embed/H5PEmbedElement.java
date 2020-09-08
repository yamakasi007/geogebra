package org.geogebra.web.full.main.embed;

import org.geogebra.common.euclidian.EuclidianController;
import org.geogebra.common.kernel.geos.GeoEmbed;
import org.geogebra.web.html5.Browser;
import org.geogebra.web.html5.util.h5pviewer.H5P;
import org.geogebra.web.html5.util.h5pviewer.H5PPaths;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

public class H5PEmbedElement extends EmbedElement{
	private final Widget widget;
	private final GeoEmbed geoEmbed;
	public static final int BOTTOM_BAR = 48;
	private final int embedId;
	public static final int SCALE = 3;
	private double initialHeight;
	private elemental2.dom.Element frame;
	private double initialRatio;
	private final EuclidianController euclidianController;
	private boolean firstUpdate=true;

	/**
	 * @param widget UI widget
	 */
	public H5PEmbedElement(Widget widget, GeoEmbed geoEmbed) {
		super(widget);
		this.widget = widget;
		this.geoEmbed = geoEmbed;
		embedId = geoEmbed.getEmbedID();
		euclidianController = geoEmbed.getApp().getActiveEuclidianView().getEuclidianController();
	}

	@Override
	public void setContent(String url) {
		render(widget, url);
	}

	private void render(Widget container, String url) {
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
		options.set("frameJs", H5PPaths.FRAME_JS);
		options.set("frameCss", H5PPaths.FRAME_CSS);
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

	@Override
	public void setSize(int contentWidth, int contentHeight) {
		resizeKeepingAspectRatio(contentWidth);
	}

	private void resizeKeepingAspectRatio(int width) {
		ensureStylebarFirstPosition();
		double h = width * initialRatio + BOTTOM_BAR;
		double ratioY = 1 / (initialHeight / h);

		Browser.scale(Js.cast(frame.parentElement), 1, ratioY, 0, 0);
	}

	protected void ensureStylebarFirstPosition() {
		if (firstUpdate) {
			euclidianController.showDynamicStylebar();
			firstUpdate = false;
		}
	}
}
