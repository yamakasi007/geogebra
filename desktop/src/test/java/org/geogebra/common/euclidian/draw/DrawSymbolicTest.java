package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GShape;
import org.geogebra.common.kernel.geos.BaseSymbolicTest;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class DrawSymbolicTest extends BaseSymbolicTest {

	@Test
	public void testIntegralDrawn() {
		GeoSymbolic symbolic = add("Integral(x^3, 0, 2)");
		DrawSymbolic draw = new DrawSymbolic(app.getActiveEuclidianView(), symbolic);
		GGraphics2D graphics = Mockito.mock(GGraphics2D.class);

		draw.draw(graphics);

		Mockito.verify(graphics).draw(any(GShape.class));
		Mockito.verify(graphics).fill(any(GShape.class));
	}

	@Test
	public void testIntegralBetweenDrawn() {
		GeoSymbolic symbolic = add("IntegralBetween(x, x^3, 0, 5)");
		DrawSymbolic draw = new DrawSymbolic(app.getActiveEuclidianView(), symbolic);
		GGraphics2D graphics = Mockito.mock(GGraphics2D.class);

		draw.draw(graphics);

		Mockito.verify(graphics).draw(any(GShape.class));
		Mockito.verify(graphics).fill(any(GShape.class));
	}
}
