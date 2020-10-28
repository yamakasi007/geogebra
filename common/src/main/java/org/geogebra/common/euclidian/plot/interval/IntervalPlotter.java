package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionEvaluator;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;

public class IntervalPlotter {
	public static final int NUMBER_OF_SAMPLES = 1500;
	private final EuclidianView view;
	private final Interval xRange;
	private final Interval yRange;
	private final boolean closed;
	private double minWidthHeight=2.0;
	private final IntervalFunctionEvaluator evaluator;
	private IntervalTupleList points;

	public IntervalPlotter(EuclidianView view, GeoFunction function) {
		this.view = view;
		xRange = new Interval();
		yRange = new Interval();
		closed = false;
		updateRanges();
		evaluator = new IntervalFunctionEvaluator(function, xRange, NUMBER_OF_SAMPLES);
		evaluator.update(xRange);
		update();
	}

	public void update() {
		updateRanges();
		points = evaluator.result();
		minWidthHeight = Math.max(points.getDeltaX(), 1);
	}

	private void updateRanges() {
		xRange.set(view.getXmin(), view.getXmax());
		yRange.set(view.getYmin(), view.getYmax());
	}

	public void draw(GGraphics2D g2) {
		Interval range = new Interval(yRange);
		double minY = range.getLow();
		double maxY = range.getHigh();
		int ox = view.toScreenCoordX(view.getXZero());
		int oy = view.toScreenCoordY(view.getYZero());
		for (IntervalTuple point: points) {
			if (point != null) {
				Interval x = point.x();
				Interval y = point.y();
//				Log.debug("sin x: " + x + " y: " + y);
				double yLow = y.getLow();
				double yHigh = y.getHigh();
				if (closed) {
					yLow = Math.min(yLow, 0);
					yHigh = Math.max(yHigh, 0);
				}
				double moveX = view.toScreenCoordX(x.getLow());
				double gLow = !Double.isInfinite(yHigh) ? yScale(yHigh) : Double.NEGATIVE_INFINITY;
				double gHigh = !Double.isInfinite(yLow) ? yScale(yLow) : Double.POSITIVE_INFINITY;
//				Log.debug("RW rect: (" + x.getLow() + ", " + gLow + ", " + x.getLow() + minWidthHeight
//				 + ", " + gHigh + ")");
				Interval viewPortY = clampRange(minY, maxY, gHigh, gLow);
				double vLow =  view.toScreenCoordY(gLow);
				double vHigh = view.toScreenCoordY(gHigh);
				int height = (int)Math.max(vLow - vHigh, minWidthHeight);
				g2.fillRect((int) moveX, (int) vLow, height,height);
			}
		}
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
