package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArithmeticTest {

	@Test
	public void testFmod() {
		Interval n = interval(5.3, 5.3).fmod(interval(2, 2));
		assertTrue(n.almostEqual(interval(1.3, 1.3)));

		n = interval(5,7).fmod(interval(2, 3));
		assertTrue(n.almostEqual(interval(2, 5)));

		n = interval(18.5, 18.5).fmod(interval(4.2, 4.2));
		assertTrue(n.almostEqual(interval(1.7, 1.7)));

		n = interval(-10, -10).fmod(interval(3, 3));
		assertTrue(n.almostEqual(interval(-1, -1)));

		n = new Interval().fmod(IntervalConstants.EMPTY);
		assertTrue(n.isEmpty());
	}
}
