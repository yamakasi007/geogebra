package org.geogebra.common.kernel.interval;

import static java.lang.Math.PI;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE_HIGH;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE_LOW;
import static org.geogebra.common.kernel.interval.IntervalConstants.ZERO;
import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntervalTrigonometricTest {

	@Test
	public void testPiTwice() {
		Interval interval = new Interval(PI_TWICE);
		assertArrayEquals(new double[] {PI_TWICE_LOW, PI_TWICE_HIGH},
				interval.toArray(), 0);
	}

	@Test
	public void testCos() {
		shouldEqual(IntervalConstants.ONE, interval(0, 0).cos());
		shouldEqual(interval(0, 1), interval(0, PI / 2).cos());
		shouldEqual(interval(-1, 1), interval(-PI, PI).cos());
		shouldEqual(interval(-1, 1), interval(0, (3 * PI) / 2).cos());
		shouldEqual(interval(-1, 0), interval(PI, (3 * PI) / 2).cos());
		shouldEqual(interval(-1, -1), interval(-PI, -PI).cos());
		shouldEqual(interval(-1, 1), interval(-PI, PI).cos());
		shouldEqual(ZERO, interval(PI / 2, PI / 2).cos());
		shouldEqual(ZERO, interval(-PI / 2, -PI / 2).cos());
		shouldEqual(interval(-1, 1), interval(-2 * PI, PI).cos());
		shouldEqual(interval(-1, 1), interval(-3 * PI / 2, PI).cos());
		shouldEqual(interval(-1, 0), interval(PI / 2, PI).cos());
		shouldEqual(interval(-1, 1), interval(-PI / 2, PI).cos());
	}

	@Test
	public void testCosWithInfinity() {
		shouldEqual(interval(-1, 1), interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).cos());
		assertTrue(interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).cos().isEmpty());
		assertTrue(interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).cos().isEmpty());
	}
}
