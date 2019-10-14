/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.kernel.statistics;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.SetRandomValue;
import org.geogebra.common.kernel.algos.AlgoTwoNumFunction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoNumberValue;
import org.geogebra.common.kernel.kernelND.GeoElementND;

/**
 * Computes RandomBinomial[a, b]
 * 
 * @author Michael Borcherds
 */
public class AlgoRandomBinomial extends AlgoTwoNumFunction
		implements SetRandomValue {

	/**
	 * @param cons
	 *            construction
	 * @param label
	 *            output label
	 * @param n
	 *            number of trials
	 * @param p
	 *            probability of success
	 */
	public AlgoRandomBinomial(Construction cons, String label, GeoNumberValue n,
			GeoNumberValue p) {
		super(cons, label, n, p);

		// output is random number
		cons.addRandomGeo(num);
	}

	@Override
	public Commands getClassName() {
		return Commands.RandomBinomial;
	}

	@Override
	public final void compute() {
		if (input[0].isDefined() && input[1].isDefined()) {
			if (b.getDouble() < 0) {
				num.setUndefined();
			} else {
				// disabled randomBinomialTRS() as it doesn't work well
				// eg when p is near 0.5
				// http://www.geogebra.org/forum/viewtopic.php?f=8&t=18685
				// num.setValue(randomBinomialTRS((int)a.getDouble(),
				// b.getDouble()));
				num.setValue(
						randomBinomial((int) a.getDouble(), b.getDouble()));
			}

		} else {
			num.setUndefined();
		}
	}

	@Override
	public boolean setRandomValue(GeoElementND d0) {
		double d = Math.round(d0.evaluateDouble());
		num.setValue(Math.max(0, Math.min(d, a.getDouble())));
		return true;
	}

	private int randomBinomial(double n, double p) {

		int count = 0;
		for (int i = 0; i < n; i++) {
			if (kernel.getApplication().getRandomNumber() < p) {
				count++;
			}
		}

		return count;
	}
}
