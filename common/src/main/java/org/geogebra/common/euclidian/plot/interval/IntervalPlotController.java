package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.CoordSystemAnimationListener;
import org.geogebra.common.euclidian.EuclidianView;

public class IntervalPlotController implements CoordSystemAnimationListener {

	private final IntervalPlotModel model;
	private final EuclidianView view;
	private final IntervalPath path;

	public IntervalPlotController(IntervalPlotModel model, EuclidianView view, IntervalPath path) {

		this.model = model;
		this.view = view;
		this.path = path;
		view.getEuclidianController().addZoomerAnimationListener(this);
	}

	@Override
	public void onZoomStop() {
		model.updateAll();
		path.update();
	}

	@Override
	public void onCoordSystemMoved(double dx, double dy) {

	}
}
