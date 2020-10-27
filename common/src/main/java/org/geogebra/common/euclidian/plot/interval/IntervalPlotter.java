package org.geogebra.common.euclidian.plot.interval;

import java.util.List;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionEvaluator;
import org.geogebra.common.kernel.interval.IntervalTuple;

public class IntervalPlotter {
	public static final int NUMBER_OF_SAMPLES = 500;
	private final GeneralPathClipped gp;
	private final EuclidianView view;
	private final Interval xRange;
	private final Interval yRange;
	private final boolean closed;
	private double minWidthHeight=1.0;
	private final IntervalFunctionEvaluator evaluator;
	private List<IntervalTuple> points;

	public IntervalPlotter(EuclidianView view, GeoFunction function, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		xRange = new Interval();
		yRange = new Interval();
		closed = false;
		updateRanges();
		evaluator = new IntervalFunctionEvaluator(function, xRange, NUMBER_OF_SAMPLES);
		update();
	}

	public void update() {
		updateRanges();
		points = evaluator.result();
		updateExamplePath();
	}

	private void updateExamplePath() {
		gp.reset();
		gp.moveTo(0, 0);
		gp.lineTo(-2, 2);
		gp.lineTo(2, -2);
		gp.lineTo(-2, -2);
		gp.closePath();
	}

	private void updateRanges() {
		xRange.set(view.getXmin(), view.getXmax());
		yRange.set(view.getYmin(), view.getYmax());
	}

	private void updatePath() {
		gp.reset();
		gp.moveTo(0,0);
		Interval range = new Interval(yRange);
		double minY = range.getLow();
		double maxY = range.getHigh();
		for (IntervalTuple point: points) {
			if (point != null) {
				Interval x = point.x();
				Interval y = point.y();
				double yLow = y.getLow();
				double yHigh = y.getHigh();
				if (closed) {
					yLow = Math.min(yLow, 0);
					yHigh = Math.max(yHigh, 0);
				}
				double moveX = xScale(xRange.getLow());
				double gLow = !Double.isInfinite(yHigh) ? yScale(yHigh) : Double.NEGATIVE_INFINITY;
				double gHigh = !Double.isInfinite(yLow) ? yScale(yHigh) : Double.POSITIVE_INFINITY;
				Interval viewPortY = clampRange(minY, maxY, gLow, gHigh);
				double vLow = viewPortY.getLow();
				double vHigh = viewPortY.getHigh();
				gp.lineTo(vHigh - vLow, minWidthHeight);
			}
		}
		gp.closePath();
	}

	private double yScale(double y) {
		return y;
	}

	private Interval clampRange (double vLow, double vHigh, double gLow, double gHigh) {
		if (gLow > gHigh) {
			double t = gLow;
			gLow = gHigh;
			gHigh = t;
		}

		double high = Math.min(vHigh, gHigh);
		double low = Math.max(vLow, gLow);
			if (low > high) {
				// no overlap
				return new Interval(-minWidthHeight, 0);
			}

			return new Interval(low, high);
	}

	private double xScale(double x) {
		return x;
	}
}
