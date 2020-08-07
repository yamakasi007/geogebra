package org.geogebra.common.geogebra3D.euclidian3D.openGL;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.util.debug.Log;

/** Class that manages wireframe drawing */
public class PlotterWireframe {

	private final Manager manager;

	private PlotterBrush brush;

	/**
	 * Default constructor
	 * @param manager manager
	 */
	public PlotterWireframe(Manager manager) {
		this.manager = manager;

		// ToDo simplify
		this.brush = manager.getBrush();
	}

	public void start(int id) {
		brush.start(id);
	}

	public int end() {
		return brush.end();
	}

	public void drawParaboloid(Coords center, Coords ev0, Coords ev1,
			Coords ev2, double r0, double r1, int longitude, double min,
			double max) {
		Log.debug("PARABOLOID");
		Log.debug(r0);
		Log.debug(r1);
		Log.debug(longitude);
		Log.debug(min);
		Log.debug(max);
		// ToDo Figure out what scale comes here
		double minMaxScale = 2;
		double t = max * minMaxScale;

		brush.setThickness(1);
		brush.setColor(GColor.BLACK);
		brush.parabola(center, ev2, ev1, r1, -t, t, null, null);
		brush.parabola(center, ev2, ev0, r0, -t, t, null, null);
	}
}
