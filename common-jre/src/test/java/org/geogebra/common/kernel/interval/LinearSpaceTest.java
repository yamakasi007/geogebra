package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class LinearSpaceTest {
	@Test
	public void testNextHundredStep10() {
		LinearSpace space = new LinearSpace(interval(0, 100), 10);
			assertEquals(Arrays.asList(0, 10, 20, 30, 40, 50, 60
			,70, 80, 90, 100), space.values());
		}
}
