package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.kernel.interval.IntervalTuple;
import org.junit.Test;

public class IntervalFunctionSamplerTest {

	@Test
	public void testRangeSplitting() {
		IntervalTuple range1 = PlotterUtils.createRange(0, 1, -2, 2);
		IntervalTuple range2 = createRange(-1, 0, -2, 2);
	}
}
