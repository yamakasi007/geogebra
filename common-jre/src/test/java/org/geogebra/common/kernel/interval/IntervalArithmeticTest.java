package org.geogebra.common.kernel.interval;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntervalArithmeticTest {

	@Test
	public void testAddition() {
		shouldEqual(interval(-2, 2),
				interval(-1, 1).add(interval(-1, 1)));
		shouldEqual(interval(-1, POSITIVE_INFINITY),
				interval(-1, POSITIVE_INFINITY).add(interval(0, 1)));
	}

	@Test
	public void testSubtraction() {
		shouldEqual(interval(-2, 2),
				interval(-1, 1).subtract(interval(-1, 1)));

		shouldEqual(interval(2, 5),
				interval(5, 7).subtract(interval(2, 3)));

		shouldEqual(interval(-2, POSITIVE_INFINITY),
				interval(-1, POSITIVE_INFINITY).subtract(interval(0, 1)));
	}

	@Test
	public void testMultiplicationWithEmpty() {
		assertTrue(new Interval().multiply(interval(1, 1)).isEmpty());
	}

	@Test
	public void testMultiplicationWithPositiveAndPositive() {
		shouldEqual(interval(2, 6),
				interval(1, 2).multiply(interval(2, 3)));

		shouldEqual(interval(4, POSITIVE_INFINITY),
				interval(1, POSITIVE_INFINITY).multiply(interval(4, 6)));

		shouldEqual(interval(POSITIVE_INFINITY, POSITIVE_INFINITY),
				interval(1, POSITIVE_INFINITY)
						.multiply(interval(POSITIVE_INFINITY, POSITIVE_INFINITY)));
	}


	@Test
	public void testMultiplicationWithPositiveAndNegative() {
		shouldEqual(interval(-6, -2),
				interval(1, 2).multiply(interval(-3, -2)));

		shouldEqual(interval(NEGATIVE_INFINITY, -2),
				interval(1, POSITIVE_INFINITY).multiply(interval(-3, -2)));
	}


	@Test
	public void testMultiplicationWithPositiveAndMixed() {
		shouldEqual(interval(-4, 6),
				interval(1, 2).multiply(interval(-2, 3)));

		shouldEqual(interval(NEGATIVE_INFINITY, POSITIVE_INFINITY),
				interval(1, POSITIVE_INFINITY).multiply(interval(-2, 3)));
	}

	@Test
	public void testMultiplicationWithPositiveAndZero() {
		shouldEqual(interval(0, 0),
				interval(1, 2).multiply(interval(0, 0)));

		shouldEqual(interval(0, 0),
				interval(1, POSITIVE_INFINITY).multiply(interval(0, 0)));
	}


}
