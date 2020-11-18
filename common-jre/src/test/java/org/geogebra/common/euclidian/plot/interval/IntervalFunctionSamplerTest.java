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
	public void testRangeSplitting() {
		IntervalTuple rangeLeft = createRange(-1, 0, -2, 2);
		IntervalTuple rangeRight = createRange(0, 1, -2, 2);
		IntervalTuple range = createRange(-1, 1, -2, 2);
		GeoFunction sinx = add("sinx");
		IntervalFunctionSampler samplerLeft = newSampler(sinx, rangeLeft, 50);
		IntervalFunctionSampler samplerRight = newSampler(sinx, rangeRight, 50);
		IntervalFunctionSampler sampler = newSampler(sinx, range, 100);
		IntervalTupleList sum = samplerLeft.result();
		sum.append(samplerRight.result());
		IntervalTupleList expected = sampler.result();
		assertEquals(expected, sum);
	}

}
