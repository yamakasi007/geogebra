package org.geogebra.common.euclidian;

public interface CoordSystemAnimationListener {

	/**
	 * Called when zoom stops animating. 
	 */
	void onZoomStop();

	/**
	 * Called when coordinate system has moved.
	 *
	 * @param dx x difference of the move
	 * @param dy y difference of the move
	 */
	void onCoordSystemMoved(double dx, double dy);
}
