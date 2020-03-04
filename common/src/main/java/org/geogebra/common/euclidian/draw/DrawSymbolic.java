package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.Drawable;
import org.geogebra.common.euclidian.DrawableND;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.geogebra.common.kernel.kernelND.GeoElementND;

/**
 * Drawable for symbolic geos
 */
public class DrawSymbolic extends Drawable {

	private GeoSymbolic symbolic;
	private DrawableND delegate;

	/**
	 * @param ev
	 *            view
	 * @param geo
	 *            symbolic geo
	 */
	public DrawSymbolic(EuclidianView ev, GeoSymbolic geo) {
		this.view = ev;
		this.geo = geo;
		this.symbolic = geo;
		update();
	}

	@Override
	public GeoElement getGeoElement() {
		return geo;
	}

	@Override
	public void update() {
		GeoElementND twinGeo = symbolic.getTwinGeo();
		if (twinGeo == null) {
			delegate = null;
		} else if (delegate != null
				&& delegate.getGeoElement() == twinGeo) {
			delegate.update();
		} else if (!createCustomDrawable()) {
			delegate = view.newDrawable(symbolic.getTwinGeo());
			if (delegate instanceof Drawable) {
				((Drawable) delegate).setTopLevelGeo(symbolic);
				delegate.update();
			}
		}
	}

	private boolean createCustomDrawable() {
		GeoElement twinGeo = (GeoElement) symbolic.getTwinGeo();
		if (symbolic.isIntegral()) {
			SymbolicGeoHelper helper = new SymbolicGeoHelper(view, geo);
			Command command = symbolic.getDefinition().getTopLevelCommand();
			GeoFunction function = helper.asFunction(command, 0);
			NumberValue a = helper.asDouble(command, 1);
			NumberValue b = helper.asDouble(command, 2);
			delegate = new DrawIntegral(view, twinGeo, function, a, b);
			((Drawable) delegate).setTopLevelGeo(symbolic);
			return true;
		} else if (symbolic.isIntegralBetween()) {
			SymbolicGeoHelper helper = new SymbolicGeoHelper(view, geo);
			Command command = symbolic.getDefinition().getTopLevelCommand();
			GeoFunction fa = helper.asFunction(command, 0);
			GeoFunction fb = helper.asFunction(command, 0);
			NumberValue a = helper.asDouble(command, 2);
			NumberValue b = helper.asDouble(command, 3);
			delegate = new DrawIntegralFunctions(view, twinGeo, fa, fb, a, b);
			((Drawable) delegate).setTopLevelGeo(symbolic);
			return true;
		}
		return false;
	}

	@Override
	public void draw(GGraphics2D g2) {
		if (delegate != null && geo.isEuclidianVisible()) {
			((Drawable) delegate).draw(g2);
		}
	}

	@Override
	public boolean hit(int x, int y, int hitThreshold) {
		if (delegate != null) {
			return ((Drawable) delegate).hit(x, y, hitThreshold);
		}
		return false;
	}

	@Override
	public boolean isInside(GRectangle rect) {
		if (delegate != null) {
			return ((Drawable) delegate).isInside(rect);
		}
		return false;
	}

}
