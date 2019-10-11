package org.geogebra.common.kernel.algos;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.cas.UsesCAS;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoBoolean;

public class AlgoCasLoaded extends AlgoElement implements UsesCAS {
	private GeoBoolean output;

	/**
	 *
	 * @param c the construction
	 */
	public AlgoCasLoaded(Construction c) {
		super(c);
		setInputOutput();
		compute();
	}

	@Override
	protected void setInputOutput() {
		output = new GeoBoolean(cons, false);
		setOutputLength(1);
		setOutput(0, output);
		input = new GeoElement[0];
		setDependencies();
	}

	@Override
	final public String toString(StringTemplate tpl) {
		return getLoc().getPlain("CASLoaded", "");
	}


	@Override
	public void compute() {
		output.setValue(kernel.isGeoGebraCASready());
	}

	@Override
	public GetCommand getClassName() {
		return Commands.CASLoaded ;
	}

	public GeoBoolean getResult() {
		return output;
	}
}
