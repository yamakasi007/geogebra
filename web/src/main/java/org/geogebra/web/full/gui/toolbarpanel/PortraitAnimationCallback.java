package org.geogebra.web.full.gui.toolbarpanel;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.main.App;
import org.geogebra.web.html5.main.AppW;

import com.google.gwt.dom.client.Style.Overflow;

/**
 * Callback for tool panel opening/closing in portrait mode
 */
public class PortraitAnimationCallback extends NavRailAnimationCallback {

	private AppW app;

	/**
	 * @param header
	 *            header panel
	 * @param app
	 *            application
	 */
	public PortraitAnimationCallback(NavigationRail header, AppW app) {
		super(header);
		this.app = app;
	}

	@Override
	protected void onStart() {
		app.getFrameElement().getStyle().setOverflow(Overflow.HIDDEN);
		if (navRail.isOpen()) {
			navRail.removeStyleName("header-close-portrait");
			navRail.addStyleName("header-open-portrait");
			navRail.toolbarPanel.onOpen();
		}
		// header.hideCenter();
	}

	@Override
	protected void onEnd() {
		app.getFrameElement().getStyle().setOverflow(Overflow.VISIBLE);
		if (!navRail.isOpen()) {
			navRail.removeStyleName("header-open-portrait");
			navRail.addStyleName("header-close-portrait");
		}

		EuclidianView ev = navRail.app.getActiveEuclidianView();
		if (ev.getViewID() == App.VIEW_EUCLIDIAN3D) {
			return;
		}
		int d = navRail.isOpen() ? -1 : 1;

		ev.translateCoordSystemForAnimation(
				d * navRail.toolbarPanel.getOpenHeightInPortrait() / 2);
	}

}
