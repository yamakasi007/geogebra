package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.MyError;

/**
 * CASLoaded[]
 * Returns if CAS subsystem is loaded or not.
 */
public class CmdCASLoaded extends CommandProcessor {
	/**
	 * Create new command processor
	 *
	 * @param kernel
	 *            kernel
	 */
	public CmdCASLoaded(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] result = {new GeoBoolean(kernel.getConstruction(),
				kernel.isGeoGebraCASready())};
		switch (n) {
		case 0:
			return result;

		default:
			throw argNumErr(c);
		}
	}

}
