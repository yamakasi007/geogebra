package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionEvaluator;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;
import org.geogebra.common.util.debug.Log;

public class IntervalPlotter {
	public static final int NUMBER_OF_SAMPLES = 1500;
	private final EuclidianView view;
	private final Interval xRange;
	private final Interval yRange;
	private final boolean closed;
	private double minWidthHeight=1.0;
	private final IntervalFunctionEvaluator evaluator;
	private IntervalTupleList points;
	private final GeneralPathClipped gp;

	public IntervalPlotter(EuclidianView view, GeoFunction function, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		xRange = new Interval();
		yRange = new Interval();
		closed = false;
		updateRanges();
		int numberOfSamples = view.getViewWidth();
		Log.debug("NumberOfSamples: " + numberOfSamples);
		evaluator = new IntervalFunctionEvaluator(function, xRange, numberOfSamples);
		evaluator.update(xRange);
		updateRanges();
//		evaluator.update(xRange);
		points = evaluator.result();
		minWidthHeight = Math.max(points.getDeltaX(), 1);
		update();
	}

	public void update() {
		updatePath();
	}

	private void updateRanges() {
		xRange.set(view.getXmin(), view.getXmax());
		yRange.set(view.getYmin(), view.getYmax());
	}

	public void updatePath() {
		if (points.isEmpty()) {
			return;
		}
		gp.reset();
		moveToFirstPoint();
		Interval lastY = new Interval(0);
		for (IntervalTuple point: points) {
			if (point != null) {
				Interval x = view.toScreenIntervalX(point.x());
				Interval y = view.toScreenIntervalY(point.y());
				plot(x, y, y.isGreaterThan(lastY));
				lastY.set(y);
			}
		}
	}

	private void moveToFirstPoint() {
		IntervalTuple point = points.get(0);
		int px = view.toScreenCoordX(point.x().getLow());
		int py = view.toScreenCoordX(point.y().getLow());
		gp.moveTo(px, py);
		gp.firstPoint();

	}

	private void plot(Interval x, Interval y, boolean greater) {

		if (greater) {
			gp.lineTo(x.getLow(), y.getLow());
			gp.lineTo(x.getHigh(), y.getHigh());
		} else {
			gp.lineTo(x.getLow(), y.getHigh());
			gp.lineTo(x.getHigh(), y.getLow());
		}
	}
}
