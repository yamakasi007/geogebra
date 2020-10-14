package org.geogebra.common.kernel.interval;

import static org.junit.Assert.*;

import org.geogebra.common.kernel.Kernel;
import org.junit.Test;

public class RMathTest {

	@Test
	public void testDivLo() {
		double n = RMath.divLo(2, 3);
		double d = 2.0 / 3.0;
		assertTrue(n < d);
		assertEquals(n, d, Kernel.MAX_PRECISION);
	}

	@Test
	public void testDivHi() {
		double n = RMath.divHi(2, 3);
		double d = 2.0 / 3.0;
		assertTrue(n > d);
		assertEquals(n, d, Kernel.MAX_PRECISION);
	}
}