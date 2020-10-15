package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;

import org.junit.Test;

public class IntervalTrigonometricTest {

	@Test
	public void testCos() {
		shouldEqual(IntervalConstants.ZERO.cos(), IntervalConstants.ONE);
		shouldEqual(interval(0, Math.PI / 2).cos(), interval(0, 1));
		shouldEqual(interval(-Math.PI, Math.PI).cos(), interval(-1, 1));
	}
}
