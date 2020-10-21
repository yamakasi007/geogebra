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

	@Test
	public void testLog() {
		shouldEqual(interval(0, 0), interval(1, 1).log());
		shouldEqual(interval(0, 3), interval(1, Math.exp(3)).log());
	}

	@Test
	public void testLog10() {
		shouldEqual(interval(0, 0), interval(1, 1).log10());
		shouldEqual(interval(0, 1), interval(1, 10).log10());
		shouldEqual(interval(0, 2), interval(1, 100).log10());
	}

	@Test
	public void testLog2() {
		shouldEqual(interval(0, 0), interval(1, 1).log2());
		shouldEqual(interval(0, 1), interval(1, 2).log2());
		shouldEqual(interval(0, 3), interval(1, 8).log2());
	}
}
