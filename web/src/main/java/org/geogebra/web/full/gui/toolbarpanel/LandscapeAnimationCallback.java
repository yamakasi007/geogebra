package org.geogebra.web.full.gui.toolbarpanel;

/**
 * Callback for tool panel opening/closing in landscape mode
 */
public class LandscapeAnimationCallback extends NavRailAnimationCallback {

	private static final int OPEN_HEIGHT = 56;

	/**
	 * @param header
	 *            header
	 */
	public LandscapeAnimationCallback(NavigationRail header) {
		super(header);
	}

	@Override
	protected void onStart() {
		navRail.hideUndoRedoPanel();
		if (navRail.isOpen()) {
			navRail.setHeight(OPEN_HEIGHT + "px");
		}
	}

	@Override
	protected void onEnd() {
		navRail.onLandscapeAnimationEnd();
	}
}
