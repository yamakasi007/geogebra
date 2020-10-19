package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;

import org.junit.Test;

public class IntervalMiscTest {

	@Test
	public void testExp() {
		shouldEqual(interval(0.36787944117, 2.71828182846), interval(-1, 1).exp());
		shouldEqual(interval(0.04978706836, 20.0855369232), interval(-3, 3).exp());
	}
}
