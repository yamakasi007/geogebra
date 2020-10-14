package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.junit.Assert.assertEquals;
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

		n = interval(2, 2).fmod(interval(2, 2));
		assertTrue(n.almostEqual(IntervalConstants.ZERO));
	}

	@Test
	public void testMultiplicativeInverse() {
		assertTrue(interval(1, 1).almostEqual(interval(1, 1).multiplicativeInverse()));
		assertTrue(interval(1 / 6.0, 1 / 2.0).almostEqual(interval(2, 6).multiplicativeInverse()));
		assertTrue(
				interval(-1 / 2.0, -1 / 6.0).almostEqual(interval(-6, -2).multiplicativeInverse()));
	}

//	@Ignore
	@Test
	public void testMultiplicativeInverseResultInfinityAbs() {
		Interval actual = interval(-6, 0).multiplicativeInverse();
		assertEquals(actual.getLow(), Double.NEGATIVE_INFINITY, 0);
		double d = 1.0 / 6.0;
		double high = actual.getHigh() + d;
		assertTrue(Math.abs(high) < 1E-7);
	}

	@Test
	public void testMultiplicativeInverseResultAbsInfinity() {
		Interval actual = interval(0, 2).multiplicativeInverse();
		assertEquals(actual.getHigh(), Double.POSITIVE_INFINITY, 0);
		double low = actual.getLow() - (1.0 / 2.0);
		assertTrue(Math.abs(low) < 1E-7);
	}

	@Test
	public void testMultiplicativeInverseResultWhole() {
		assertEquals(IntervalConstants.WHOLE, interval(-6, 2).multiplicativeInverse());
	}

	@Test
	public void testMultiplicativeInverseZero() {
		assertTrue(IntervalConstants.ZERO.multiplicativeInverse().isEmpty());
	}
}
