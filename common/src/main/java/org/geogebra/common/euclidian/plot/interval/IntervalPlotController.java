package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.CoordSystemAnimationListener;
import org.geogebra.common.euclidian.EuclidianView;

public class IntervalPlotController implements CoordSystemAnimationListener {

	private final IntervalPlotModel model;
	private final EuclidianView view;

	public IntervalPlotController(IntervalPlotModel model, EuclidianView view) {
		this.model = model;
		this.view = view;
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
		moveDomain();
	}

	public void moveDomain() {
		model.moveDomain(view.domain());
	}
}
