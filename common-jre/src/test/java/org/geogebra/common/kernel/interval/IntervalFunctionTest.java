package org.geogebra.common.kernel.interval;

import static java.lang.Math.PI;
import static org.geogebra.common.kernel.interval.IntervalTest.interval;
import static org.geogebra.common.kernel.interval.IntervalTest.shouldEqual;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.junit.Test;

public class IntervalFunctionTest extends BaseUnitTest {

	@Test
	public void evaluateSin() throws Exception {
		GeoFunction geo = add("sin(x)");
		IntervalFunction function = new IntervalFunction(geo);
		shouldEqual(interval(0, 1), function.evaluate(interval(0, PI / 2)));
	}

	@Test
	public void evaluate2Sin() throws Exception {
		GeoFunction geo = add("2 * sin(x)");
		IntervalFunction function = new IntervalFunction(geo);
		shouldEqual(interval(-2, 2),
				function.evaluate(interval(-PI, PI)));
	}

	@Test
	public void evaluateSin2x() throws Exception {
		GeoFunction geo = add("sin(2x)");
		geo.value(2);
		IntervalFunction function = new IntervalFunction(geo);
		shouldEqual(interval(-1, 1),
				function.evaluate(interval(-PI, PI)));
	}

	@Test
	public void testDependencyProblem() {
		assertTrue(IntervalFunction.hasDependencyProblem(add("x^2 + x")));
		assertTrue(IntervalFunction.hasDependencyProblem(add("abs(x)/x")));
		assertTrue(IntervalFunction.hasDependencyProblem(add("(1/x)sin(x)")));
		assertFalse(IntervalFunction.hasDependencyProblem(add("sin(x^4)")));
		assertFalse(IntervalFunction.hasDependencyProblem(add("tan(x)/2")));
		assertFalse(IntervalFunction.hasDependencyProblem(add("x+3")));
	}
}