package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.CoordSystemAnimationListener;
import org.geogebra.common.euclidian.CoordSystemInfo;
import org.geogebra.common.euclidian.EuclidianController;
import org.geogebra.common.util.debug.Log;

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
	 */
	public IntervalPlotController(IntervalPlotModel model) {
		this.model = model;
	}

	/**
	 * @param controller {@link EuclidianController}
	 */
	public void attachEuclidianController(EuclidianController controller) {
		controller.addZoomerAnimationListener(this);
	}

	@Override
	public void onZoomStop(CoordSystemInfo info) {
		info.setAxisZoom(false);
		model.updateAll();
	}

	@Override
	public void onMoveStop() {
		model.updateAll();
	}

	@Override
	public void onMove(CoordSystemInfo info) {
		if (info.isAxisZoom()) {
			Log.debug("Axis zoom - onMove() canceled");
			return;
		}
		model.updateDomain();
		Log.debug(info);
		Log.debug("Points: " + model.getPoints().count());
	}
}