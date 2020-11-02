package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.CoordSystemListener;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;
import org.geogebra.common.util.debug.Log;

public class IntervalPlotter implements CoordSystemListener {
	private final EuclidianView view;
	private final IntervalFunctionSampler evaluator;
	private IntervalTupleList points;
	private IntervalTuple range;
	private final GeneralPathClipped gp;
	private double yscale;

	public IntervalPlotter(EuclidianView view, GeoFunction function, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		range = new IntervalTuple();
		updateRanges();
		int numberOfSamples = view.getWidth();
		Log.debug("NumberOfSamples: " + numberOfSamples);
		evaluator = new IntervalFunctionSampler(function, range, numberOfSamples);
		view.getEuclidianController().addZoomerListener(this);
		updateEvaluator();
		update();
	}

	public void update() {
		updatePath();
	}

	private void updateRanges() {
		range.x().set(view.getXmin(), view.getXmax());
		range.y().set(view.getYmin(), view.getYmax());
	}

	public void updatePath() {
		if (points.isEmpty()) {
			return;
		}
		gp.reset();

		Interval lastY = new Interval();
		for (IntervalTuple point: points) {
			if (point != null) {
				plotInterval(lastY, point);
			}
		}
	}

	private void plotInterval(Interval lastY, IntervalTuple point) {
		Interval x = view.toScreenIntervalX(point.x());
		Interval y = view.toScreenIntervalY(point.y());
		if (y.isGreaterThan(lastY)) {
			plotHigh(x, y);
		} else {
			plotLow(x, y);
		}

		lastY.set(y);
	}

	private void plotHigh(Interval x, Interval y) {
		lineTo(x.getLow(), y.getLow());
		lineTo(x.getHigh(), y.getHigh());
	}

	private void plotLow(Interval x, Interval y) {
		lineTo(x.getLow(), y.getHigh());
		lineTo(x.getHigh(), y.getLow());
	}

	private void lineTo(double low, double high) {
		gp.lineTo(low, high);
//		Log.debug("[PATH] L " + (int)low + ", " + (int)high);
	}

	@Override
	public void onCoordSystemChanged() {
		if (!view.isZoomerRunning()) {
			updateEvaluator();
		}
	}

	public void updateEvaluator() {
		updateRanges();
		evaluator.update(range);
		points = evaluator.result();
		updatePath();
		yscale = view.getYscale();
	}

}