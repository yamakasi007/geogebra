package org.geogebra.common.kernel.interval;

import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.euclidian.EuclidianView;
import org.junit.Before;
import org.junit.Test;

public class LinearSpaceTest extends BaseUnitTest {

	private EuclidianView view;

	@Before
	public void setUp() {
		view = getApp().getActiveEuclidianView();
	}

	@Test
	public void testExtendToInt() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(8, 18, 10);
		space.extendMax(18);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testExtendToReal() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(5, 15, 10);
		space.extendMax(14.5);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testShrinkToInt() {
		LinearSpace space = new LinearSpace(8, 10, 10);
		LinearSpace expected = new LinearSpace(0, 10, 10);
		space.extendMin(0);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testShrinkToMinusInt() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(-2, 8, 10);
		space.extendMin(-2);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testShrinkToReal() {
		LinearSpace space = new LinearSpace(8, 18, 10);
		LinearSpace expected = new LinearSpace(-2, 8, 10);
		space.extendMin(-1.4);
		assertEquals(expected.values(), space.values());
	}
}
