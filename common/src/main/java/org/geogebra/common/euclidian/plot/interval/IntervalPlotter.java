package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;

/**
 * Function plotter based on interval arithmetic
 *
 * @author laszlo
 */
public class IntervalPlotter {
	private final EuclidianView view;
	private final GeneralPathClipped gp;
	private boolean enabled;
	private IntervalPlotModel model;
	private IntervalPath path;
	private IntervalPlotController controller;

	/**
	 * Creates a disabled plotter
	 */
	public IntervalPlotter(EuclidianView view, GeneralPathClipped gp) {
		this.view = view;
		this.gp = gp;
		this.enabled = false;
	}

	/**
	 * Enables plotter
	 */
	public void enableFor(GeoFunction function) {
		enabled = true;
		createModel(function);
		createPath();
		createController();
		model.updateAll();
		path.update();
	}

	private void createController() {
		controller = new IntervalPlotController(model, view, path);
	}

	private void createPath() {
		path = new IntervalPath(gp, view, model);
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
		path.update();
	}

	/**
	 * Updates and recomputes all.
	 */
	public void updateEvaluator() {
		model.updateAll();
		path.update();
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
}
