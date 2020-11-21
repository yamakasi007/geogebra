package org.geogebra.common.kernel.interval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinearSpaceTest {
	@Test
	public void testAppend() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(8, 18, 10);
		space.appendKeepSize(1.5);
		space.appendKeepSize(1.5);
		space.appendKeepSize(1.5);
		space.appendKeepSize(1.5);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testPrepend() {
		LinearSpace space = new LinearSpace(8, 18, 10);
		LinearSpace expected = new LinearSpace(0, 10, 10);
		space.prependKeepSize(1.5);
		space.prependKeepSize(1.5);
		space.prependKeepSize(1.5);
		space.prependKeepSize(1.5);
		assertEquals(expected.values(), space.values());
	}

}
