package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.euclidian.CoordSystemAnimationListener;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;

/**
 * Function plotter based on interval arithmetic
 *
 * @author laszlo
 */
public class IntervalPlotter implements CoordSystemAnimationListener {
	private final EuclidianView view;
	private final GeneralPathClipped gp;
	private boolean enabled;
	private boolean moveTo;
	private IntervalPlotModel model;

	/**
	 * Creates a disabled plotter
	 */
	public IntervalPlotter(EuclidianView view, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		this.enabled = false;
		view.getEuclidianController().addZoomerAnimationListener(this);
	}

	/**
	 * Enables plotter
	 */
	public void enableFor(GeoFunction function) {
		createModel(function);
		model.updateAll();
		updatePath();
		enabled = true;
	}

	private void createModel(GeoFunction function) {
		IntervalTuple range = new IntervalTuple();
		int numberOfSamples = view.getWidth();
		IntervalFunctionSampler sampler =
				new IntervalFunctionSampler(function, range, numberOfSamples);
		model = new IntervalPlotModel(range, sampler, view);
	}

	/**
	 * Update path to draw.
	 */
	public void update() {
		updatePath();
	}

	private void updatePath() {
		if (model.isEmpty()) {
			return;
		}

		gp.reset();

		Interval lastY = new Interval();
		for (IntervalTuple point: model.getPoints()) {
			if (point != null) {
				plotInterval(lastY, point);
			}

			moveTo = point == null;
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
		if (moveTo) {
			gp.moveTo(x.getLow(), y.getLow());
		} else {
			lineTo(x.getLow(), y.getLow());
		}

		lineTo(x.getHigh(), y.getHigh());
	}

	private void plotLow(Interval x, Interval y) {
		if (moveTo) {
			gp.moveTo(x.getLow(), y.getHigh());
		} else {
			lineTo(x.getLow(), y.getHigh());
		}
		lineTo(x.getHigh(), y.getLow());
	}

	private void lineTo(double low, double high) {
		gp.lineTo(low, high);
	}

	/**
	 * Updates and recomputes all.
	 */
	public void updateEvaluator() {
		model.updateAll();
		updatePath();
	}

	/**
	 * Draws result to Graphics
	 *
	 * @param g2 {@link GGraphics2D}
	 */
	public void draw(GGraphics2D g2) {
		g2.draw(gp);
	}

	/**
	 *
	 * @return if plotter is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void onZoomStop() {
		updateEvaluator();
	}

	@Override
	public void onCoordSystemMoved(double dx, double dy) {

	}
}
