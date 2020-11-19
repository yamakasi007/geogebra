package org.geogebra.common.euclidian.plot.interval;

import static org.geogebra.common.euclidian.plot.interval.PlotterUtils.createModel;
import static org.geogebra.common.euclidian.plot.interval.PlotterUtils.createRange;
import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.junit.Before;
import org.junit.Test;

public class IntervalPlotterTest extends BaseUnitTest {
	public static final int NUMBER_OF_SAMPLES = 20;
	private IntervalPlotController controller;
	private AppCommon app;
	private EuclidianView view;

	@Before
	public void setUp() {
		app = getApp();
		view = getApp().getActiveEuclidianView();
	}

	@Test
	public void testMoveSinBy() {
		IntervalPathPlotterMock plotter = new IntervalPathPlotterMock();
		IntervalPathPlotterMock plotterExpected = new IntervalPathPlotterMock();
		IntervalPlotModel model = newSinModel(-5.0, 5.0, plotter);
		IntervalPlotModel expectedModel = newSinModel(-4.5, 5.5, plotterExpected);
		IntervalPlotController controller = new IntervalPlotController(model, view);
		controller.moveXByPixel(15);
		assertEquals(plotter.getLog(), plotterExpected.getLog());
	}

	private IntervalPlotModel newSinModel(double low, double high, IntervalPathPlotterMock gp) {
		IntervalTuple range = createRange(low, high, -3.0, 3.0);
		IntervalPlotModel model = createModel(range, createSampler("sin(x)",
				range), view);
		IntervalPath path = new IntervalPath(gp, view, model);
		model.setPath(path);
		model.updateSampler();
		model.updatePath();
		return model;

	}

	private IntervalFunctionSampler createSampler(String functionString,
			IntervalTuple range) {
		GeoFunction function = add(functionString);
		return new IntervalFunctionSampler(function, range, NUMBER_OF_SAMPLES);
	}
}
