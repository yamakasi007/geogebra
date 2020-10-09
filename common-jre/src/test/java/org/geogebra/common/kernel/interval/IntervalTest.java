package org.geogebra.common.kernel.interval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntervalTest {

	@Test
	public void testAdd() {
		Interval interval = new Interval(-3, 2);
		Interval argument = new Interval(4, 7);
		Interval result = new Interval(1, 9);
		interval.add(argument);
		assertEquals(result, interval);
	}

	@Test
	public void testSub() {
		Interval interval = new Interval(-1, 3);
		Interval argument = new Interval(-2, 4);
		Interval result = new Interval(-5, 5);
		interval.sub(argument);
		assertEquals(result, interval);
	}
}
