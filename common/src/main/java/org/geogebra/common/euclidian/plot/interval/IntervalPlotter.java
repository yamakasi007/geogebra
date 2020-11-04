package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;

/**
 * Function plotter based on interval arithmetic
 *
 * @author laszlo
 */
public class IntervalPlotter {
	private final EuclidianView view;
	private final IntervalFunctionSampler evaluator;
	private IntervalTupleList points;
	private final IntervalTuple range;
	private final GeneralPathClipped gp;

	/**
	 *
	 * @param view to draw on.
	 * @param function to draw.
	 * @param gp GeneralPath is used to draw to.
	 */
	public IntervalPlotter(EuclidianView view, GeoFunction function, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		range = new IntervalTuple();
		updateRanges();
		int numberOfSamples = view.getWidth();
		evaluator = new IntervalFunctionSampler(function, range, numberOfSamples);
		updateEvaluator();
		update();
	}

	/**
	 * Update path to draw.
	 */
	public void update() {
		updatePath();
	}

	private void updateRanges() {
		range.x().set(view.getXmin(), view.getXmax());
		range.y().set(view.getYmin(), view.getYmax());
	}

	private void updatePath() {
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
	}

	/**
	 * Updates and recomputes all.
	 */
	public void updateEvaluator() {
		updateRanges();
		evaluator.update(range);
		points = evaluator.result();
		updatePath();
	}
}
