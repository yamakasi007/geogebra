package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.Drawable;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.GeneralPathClipped;
import org.geogebra.common.euclidian.plot.interval.IntervalPlotter;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.kernelND.CurveEvaluable;

/**
 * Draw curve using interval arithmetic.
 */
public class DrawIntervalCurve extends Drawable {
	private final GeneralPathClipped gp;
	private final IntervalPlotter plotter;
	private final GeoFunction function;

	/**
	 *
	 * @param view EuclidianView to draw on.
	 * @param curve to draw.
	 * @param gp GeneralPath to draw on.
	 */
	public DrawIntervalCurve(EuclidianView view, CurveEvaluable curve,
			GeneralPathClipped gp) {
		this.gp = gp;
		function = (GeoFunction) curve.toGeoElement();
		plotter = new IntervalPlotter(view, function, gp);
	}

	@Override
	public void update() {
		plotter.update();
	}

	@Override
	public void draw(GGraphics2D g2) {
		g2.draw(gp);
	}

	@Override
	public boolean hit(int x, int y, int hitThreshold) {
		return false;
	}

	@Override
	public boolean isInside(GRectangle rect) {
		return false;
	}

	@Override
	public GeoElement getGeoElement() {
		return function;
	}
}
