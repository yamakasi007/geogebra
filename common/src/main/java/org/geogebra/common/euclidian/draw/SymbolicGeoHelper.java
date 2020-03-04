package org.geogebra.common.euclidian.draw;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.ExpressionValue;
import org.geogebra.common.kernel.arithmetic.Function;
import org.geogebra.common.kernel.arithmetic.MyDouble;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.geos.GeoCasCell;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoSymbolic;

/**
 * Helper class to get/create relevant objects for drawing.
 */
public class SymbolicGeoHelper {

	private EuclidianView view;
	private GeoElement geo;

	/**
	 * Create a new helper.
	 *
	 * @param view view
	 * @param geo geo
	 */
	public SymbolicGeoHelper(EuclidianView view, GeoElement geo) {
		this.view = view;
		this.geo = geo;
	}

	/**
	 * @param cmd
	 *            command
	 * @param i
	 *            index
	 * @return i-th argument of command as function
	 */
	protected GeoFunction asFunction(Command cmd, int i) {
		ExpressionValue argument = cmd.getArgument(i).unwrap();
		if (argument instanceof GeoCasCell) {
			// https://help.geogebra.org/topic/integraaltussen-wordt-grafisch-verkeerd-weergegeven-via-cas
			return (GeoFunction) ((GeoCasCell) argument).getTwinGeo();
		}
		if (argument instanceof GeoSymbolic) {
			return (GeoFunction) ((GeoSymbolic) argument).getTwinGeo();
		}
		return new GeoFunction(
				view.getApplication().getKernel().getConstruction(),
				new Function(geo.getKernel(),
						cmd.getArgument(i).wrap().replaceCasCommands()));
	}

	/**
	 * @param cmd
	 *            command
	 * @param i
	 *            index
	 * @return i-th argument of command as MyDouble
	 */
	protected NumberValue asDouble(Command cmd, int i) {
		ExpressionValue argument = cmd.getArgument(i).unwrap();
		if (argument instanceof GeoCasCell) {
			return new MyDouble(cmd.getKernel(),
					((GeoCasCell) argument).getTwinGeo().evaluateDouble());
		}
		if (argument instanceof GeoSymbolic) {
			return new MyDouble(cmd.getKernel(),
					((GeoSymbolic) argument).getTwinGeo().evaluateDouble());
		}
		return new MyDouble(cmd.getKernel(), cmd.getArgument(i).wrap()
				.replaceCasCommands().evaluateDouble());
	}
}
