package org.geogebra.common.euclidian.plot.interval;

import static org.geogebra.common.euclidian.plot.interval.PlotterUtils.createRange;
import static org.geogebra.common.euclidian.plot.interval.PlotterUtils.newSampler;
import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;
import org.junit.Test;

public class IntervalFunctionSamplerTest extends BaseUnitTest {

	@Test
	public void testAppend() {
		IntervalTuple rangeActual = createRange(-5, 5, -1, 1);
		IntervalTuple rangeExpected = createRange(-3, 7, -1, 1);
		GeoFunction xDoubled = add("2x");
		IntervalFunctionSampler sampler = newSampler(xDoubled, rangeActual, 10);
		IntervalTupleList points = sampler.result();
		IntervalTupleList newPoints = sampler.append(2);

		if (newPoints != null) {
			points.append(newPoints);
		}

		IntervalFunctionSampler samplerExpected = newSampler(xDoubled,	rangeExpected,	10);

		IntervalTupleList expected = samplerExpected.result();
		assertEquals(expected, points);
	}
}
