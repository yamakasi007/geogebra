package org.geogebra.common.kernel.interval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntervalTest {

	@Test
	public void testAdd() {
		assertEquals(interval(1, 9),
				interval(-3, 2)
						.add(interval(4, 7)));
	}

	private Interval interval(double low, double high) {
		return new Interval(low, high);
	}

	@Test
	public void testSub() {
		assertEquals(interval(-5, 5),
				interval(-1, 3)
						.sub(interval(-2, 4)));
	}

	@Test
	public void testMultiplication1() {
		assertEquals(interval(-5, 10),
				interval(-1, 2)
						.multiply(interval(3, 5)));
	}

	@Test
	public void testMultiplication2() {
		assertEquals(interval(-8, -3),
				interval(-4, -3)
						.multiply(interval(1, 2)));

	}

	@Test
	public void testMultiplication3() {
		assertEquals(interval(-15, 10),
				interval(-5, 1)
						.multiply(interval(-2, 3)));

	}

	@Test
	public void testMultiplication4() {
		assertEquals(interval(-12, 6),
				interval(-1, 2)
						.multiply(interval(-6, -4)));

	}

	@Test
	public void testMultiplication5() {
		assertEquals(interval(0, 7),
				interval(-7, -5)
						.multiply(interval(-1, 0)));

	}

	@Test
	public void testMultiplyThreeIntervals() {
		assertEquals(interval(-30, 60),
				interval(-3, 1)
						.multiply(interval(-2, 4))
						.multiply(interval(-5, -1)));

	}

	@Test(expected = IntervalDivisionByZero.class)
	public void testDivisionByZero() throws IntervalDivisionByZero {
		interval(-1/3.0, 1/4.0)
				.div(interval(-1.0, 1.0));
	}

	@Test
	public void testDivision() throws IntervalDivisionByZero {
		assertEquals(interval(-2/3.0, 1/2.0),
				interval(-1/3.0, 1/4.0)
						.div(interval(1/2.0, 3/4.0)));

	}
}
