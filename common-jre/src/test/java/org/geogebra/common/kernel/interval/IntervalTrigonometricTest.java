package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalConstants.ZERO;
import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntervalTrigonometricTest {

	@Test
	public void testCos() {
		shouldEqual(interval(0, 0).cos(), IntervalConstants.ONE);
		shouldEqual(interval(0, Math.PI / 2).cos(), interval(0, 1));
		shouldEqual(interval(-Math.PI, Math.PI).cos(), interval(-1, 1));
		shouldEqual(interval(0, (3 * Math.PI) / 2).cos(), interval(-1, 1));
		shouldEqual(interval(Math.PI, (3 * Math.PI) / 2).cos(), interval(-1, 0));
		shouldEqual(interval(-Math.PI, -Math.PI).cos(), interval(-1, -1));
		shouldEqual(interval(-Math.PI, Math.PI).cos(), interval(-1, 1));
		shouldEqual(interval(Math.PI / 2, Math.PI / 2).cos(), ZERO);
		shouldEqual(interval(-Math.PI / 2, -Math.PI / 2).cos(), ZERO);
		shouldEqual(interval(-2 * Math.PI, Math.PI).cos(), interval(-1, 1));
		shouldEqual(interval(-3 * Math.PI / 2, Math.PI).cos(), interval(-1, 1));
		shouldEqual(interval(Math.PI / 2, Math.PI).cos(), interval(-1, 0));
	}

	@Test
	public void testCosMinusPi2Pi() {
		shouldEqual(interval(-Math.PI / 2, Math.PI).cos(), interval(-1, 1));
	}

	@Test
	public void testCosWithInfinity() {
//		shouldEqual(interval(-1, 1), interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
		assertTrue(interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).isEmpty());
		assertTrue(interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isEmpty());
	}
}
