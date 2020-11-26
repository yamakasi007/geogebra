package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.CoordSystemAnimationListener;
import org.geogebra.common.euclidian.EuclidianView;

/**
 * Controller for Interval Plotter to handle zoom and moving the view.
 *
 * @author laszlo
 */
public class IntervalPlotController implements CoordSystemAnimationListener {

	private final IntervalPlotModel model;

	/**
	 * Constructor.
	 * @param model {@link IntervalPlotModel}
	 * @param view {@link EuclidianView}
	 */
	public IntervalPlotController(IntervalPlotModel model, EuclidianView view) {
		this.model = model;
		view.getEuclidianController().addZoomerAnimationListener(this);
	}

	@Override
	public void onZoomStop() {
		model.updateAll();
	}

	@Override
	public void onCoordSystemMoved(double dx, double dy) {
		if (dx == 0) {
			return;
		}
		model.updateDomain();
	}
}