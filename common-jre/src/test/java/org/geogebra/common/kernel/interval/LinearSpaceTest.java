package org.geogebra.common.kernel.interval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinearSpaceTest {

	@Test
	public void testShiftRight() {
		LinearSpace expected = new LinearSpace();
		LinearSpace actual = new LinearSpace();
		actual.update(new Interval(0.0, 10.5), 10);
		expected.update(new Interval(2.5, 13.0), 10);
		actual.shiftBy(2.5);
		assertEquals(expected, actual);
	}

	@Test
	public void testShiftLeft() {
		LinearSpace expected = new LinearSpace();
		LinearSpace actual = new LinearSpace();
		actual.update(new Interval(0.0, 10.5), 10);
		expected.update(new Interval(-2.5, 8.0), 10);
		actual.shiftBy(-2.5);
		assertEquals(expected, actual);
	}
}
