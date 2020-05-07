package org.geogebra.common.kernel.geos;

import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.io.XmlTestUtil;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.junit.Test;

public class GeoInlineTableTest extends BaseUnitTest {

	private static final String COMPATIBILITY_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ "<geogebra format=\"5.0\" version=\"5.0.580.0\" app=\"notes\" >\n"
			+ "<gui>\n"
			+ "\t<font  size=\"16\"/>\n"
			+ "</gui>\n"
			+ "<construction>\n"
			+ "<expression label=\"text1\" exp=\"&quot;GeoGebra Rocks&quot;\"/>\n"
			+ "<element type=\"table\" label=\"testTable\">\n"
			+ "\t<show object=\"true\" label=\"true\">\n"
			+ "\t<objColor r=\"0\" g=\"0\" b=\"0\" alpha=\"0\"/>\n"
			+ "\t<layer val=\"0\"/>\n"
			+ "\t<labelMode val=\"4\"/>\n"
			+ "\t<startPoint  x=\"5\" y=\"6\" z=\"1\"/>\n"
			+ "\t<dimensions width=\"200.0\" height=\"72.0\" angle=\"0.0\"/>\n"
			+ "</element>\n"
			+ "</construction>\n"
			+ "</geogebra>";

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

	/*@Test
	public void loadingOldXmlShouldProduceInlineTexts() {
		getApp().setXML(COMPATIBILITY_XML, true);

		GeoInlineText loadedInlineText = (GeoInlineText) lookup("text1");

		assertEquals(5, loadedInlineText.getLocation().getX(), Kernel.MAX_PRECISION);
		assertEquals(6, loadedInlineText.getLocation().getY(), Kernel.MAX_PRECISION);
		assertEquals(150, loadedInlineText.getWidth(), Kernel.MAX_PRECISION);
		assertEquals(50, loadedInlineText.getHeight(), Kernel.MAX_PRECISION);
		assertEquals("[{\"text\":\"GeoGebra Rocks\",\"bold\":true}]",
				loadedInlineText.getContent());
	}*/
}
