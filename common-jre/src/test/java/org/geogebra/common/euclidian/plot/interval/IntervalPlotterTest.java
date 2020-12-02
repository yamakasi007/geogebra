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
	public static final int NUMBER_OF_SAMPLES = 100;
	private IntervalPlotController controller;
	private AppCommon app;
	private EuclidianView view;

	@Before
	public void setUp() {
		app = getApp();
		view = getApp().getActiveEuclidianView();
	}

	@Test
	public void testMoveRight() {
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

		assertEquals(expectedModel.getPoints(), model.getPoints());
	}

	@Test
	public void testMoveUpRightAndBack() {
		view.setRealWorldCoordSystem(-15, 15, -9, 9);
		IntervalPathPlotterMock path = new IntervalPathPlotterMock();
		IntervalPlotModel model = newModel("sin(x)",
				-15, 15, -9, 9, path);
		model.updateAll();
		moveModel(model, 2,2);
		IntervalPathPlotterMock pathExpected = new IntervalPathPlotterMock();
		IntervalPlotModel expectedModel = newModel("sin(x)",
				-13, 17, -7, 11, pathExpected);
		expectedModel.updateAll();
		assertEquals(expectedModel.getPoints().domain(), model.getPoints().domain());
	}

	private void moveModel(IntervalPlotModel model,
			double dx, double dy) {
		double xmin = view.getXmin() + dx;
		double xmax = view.getXmax() + dx;
		double ymin = view.getYmin() + dy;
		double ymax = view.getYmax() + dy;
		view.setRealWorldCoordSystem(xmin, xmax, ymin, ymax);
		model.updatePath();
	}

	@Test
	public void testSqrtSin() {
		view.setCoordSystem(0, 2, 50, 50);
		IntervalPathPlotterMock path = new IntervalPathPlotterMock();
		IntervalPlotModel model = newModel("sqrt(sin(x))", 0, 7, path);
		model.updateAll();
	}

	private IntervalPlotModel newModel(String functionString,
			double low, double high, IntervalPathPlotterMock gp) {
		return newModel(functionString, low, high, -3, 3, gp);
	}

	private IntervalPlotModel newModel(String functionString,
			double xLow, double xHigh, double yLow, double yHigh, IntervalPathPlotterMock gp) {
		IntervalTuple range = createRange(xLow, xHigh, yLow, yHigh);
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
