package org.geogebra.common.kernel.geos;

import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.io.XmlTestUtil;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.junit.Test;

public class GeoInlineTableTest extends BaseUnitTest {

	@Test
	public void inlineTableCorrectlySavedAndLoaded() {
		final double x = 1.2;
		final double y = 2.5;
		final int width = 1848;
		final int height = 1956;
		final double angle = 2.7182;

		Construction cons = getApp().getKernel().getConstruction();

		GPoint2D startPoint = new GPoint2D(x, y);

		GeoInlineTable savedInlineTable = new GeoInlineTable(cons, startPoint);
		savedInlineTable.setWidth(width);
		savedInlineTable.setHeight(height);
		savedInlineTable.setLabel("testTable");
		savedInlineTable.setAngle(angle);

		String appXML = getApp().getXML();
		XmlTestUtil.testCurrentXML(getApp());
		getApp().setXML(appXML, true);

		GeoInlineTable loadedInlineTable = (GeoInlineTable) lookup("testTable");

		assertEquals(x, loadedInlineTable.getLocation().getX(), Kernel.MAX_PRECISION);
		assertEquals(y, loadedInlineTable.getLocation().getY(), Kernel.MAX_PRECISION);
		assertEquals(width, loadedInlineTable.getWidth(), Kernel.MAX_PRECISION);
		assertEquals(height, loadedInlineTable.getHeight(), Kernel.MAX_PRECISION);
		assertEquals(angle, loadedInlineTable.getAngle(), Kernel.MAX_PRECISION);
	}
}
