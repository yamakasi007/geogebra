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
		gp.reset();
		double lastY = Double.POSITIVE_INFINITY;
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
				lastY = plot(view.toScreenIntervalX(x), y, lastY);
			}
		}
	}

	private double plot(Interval x, Interval y, double lastY) {
		double gLow = !Double.isInfinite(y.getHigh()) ? y.getHigh() : Double.NEGATIVE_INFINITY;
		double gHigh = !Double.isInfinite(y.getLow()) ? y.getLow() : Double.POSITIVE_INFINITY;
		double vLow =  view.toScreenCoordY(gLow);
		double vHigh = view.toScreenCoordY(gHigh);
		if (lastY > vLow) {
			gp.moveTo(x.getLow(), vHigh);
			gp.lineTo(x.getHigh(), vLow);
		} else {
			gp.moveTo(x.getHigh(), vHigh);
			gp.lineTo(x.getLow(), vLow);
		}

		return vLow;
 	}
}
