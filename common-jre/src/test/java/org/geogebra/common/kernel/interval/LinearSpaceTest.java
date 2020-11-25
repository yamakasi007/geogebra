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
	public void testAppendNoMod() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(8, 18, 10);
		space.appendKeepSize(4);
		space.appendKeepSize(4);
		assertEquals(expected.values(), space.values());
	}

	@Test
	public void testAppendMod() {
		LinearSpace space = new LinearSpace(0, 10, 10);
		LinearSpace expected = new LinearSpace(4, 14, 10);
		space.appendKeepSize(2.1);
		space.appendKeepSize(0.6);
		assertEquals(expected.values(), space.values());
		// -2.96, 3.080000000000001
	}

	@Test
	public void testAppendReal() {
		int start = view.toScreenCoordX(3.05);
		int end = view.toScreenCoordX(4.05);
		int diff = end - start;
		int k = 1;
		int count = 300;
		LinearSpace space = new LinearSpace(start, end, count);

		for (int i = 0; i < k; i++) {
			space.appendKeepSize(diff);
		}

		LinearSpace expsp = new LinearSpace(start + (k * diff), end + (k * diff), count);
		assertEquals(expsp, space);
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
