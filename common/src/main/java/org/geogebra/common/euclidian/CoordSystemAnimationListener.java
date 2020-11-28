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
	void onMoved(double dx, double dy);

	/**
	 * Called when coordinate system stops moving.
	 */
	void onMoveStop();
}
