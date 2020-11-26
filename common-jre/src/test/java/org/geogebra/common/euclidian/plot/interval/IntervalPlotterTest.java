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
	public static final int NUMBER_OF_SAMPLES = 5;
	private IntervalPlotController controller;
	private AppCommon app;
	private EuclidianView view;

	@Before
	public void setUp() {
		app = getApp();
		view = getApp().getActiveEuclidianView();
	}

	@Test
	public void testMoveRiht() {
		view.setRealWorldCoordSystem(0, 5, -20, 20);
		IntervalPathPlotterMock path = new IntervalPathPlotterMock();
		IntervalPlotModel model = newModel("sin(x)", 0, 5, path);
		model.updateAll();
		view.setRealWorldCoordSystem(2, 7, -20, 20);
		model.updateDomain();
		model.updatePath();

		IntervalPathPlotterMock pathExpected = new IntervalPathPlotterMock();
		IntervalPlotModel expectedModel = newModel("sin(x)", 2.0, 7.0, pathExpected);
		expectedModel.updateAll();

		assertEquals(expectedModel.getPoints()
				, model.getPoints());
	}
	@Test
	public void testMoveSinBy() {
		IntervalPathPlotterMock path = new IntervalPathPlotterMock();
		IntervalPathPlotterMock pathExpected = new IntervalPathPlotterMock();
		view.setRealWorldCoordSystem(-10, 10, -10, 10);
		IntervalPlotModel model = newModel("2x", -10.0, 10.0, path);
		IntervalPlotModel expectedModel = newModel("2x", -10.0, 8.0, pathExpected);
		model.updateAll();
		expectedModel.updateAll();
		model.updateDomain();
		model.updateDomain();
		model.updatePath();
		assertEquals(expectedModel.getPoints()
				, model.getPoints());
	}

	private IntervalPlotModel newModel(String functionString,
			double low, double high, IntervalPathPlotterMock gp) {
		IntervalTuple range = createRange(low, high, -3.0, 3.0);
		IntervalPlotModel model = createModel(range, createSampler(functionString,
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
