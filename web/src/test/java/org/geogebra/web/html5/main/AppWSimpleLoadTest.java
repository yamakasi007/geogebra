package org.geogebra.web.html5.main;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.geogebra.common.kernel.geos.GeoBoolean;
import org.geogebra.web.html5.euclidian.EuclidianSimplePanelW;
import org.geogebra.web.test.AppMocker;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import com.himamis.retex.renderer.web.graphics.JLMContext2d;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({TextAreaElement.class, EuclidianSimplePanelW.class
	, JLMContext2d.class, RootPanel.class})
public class AppWSimpleLoadTest {
	private static final String jsonPath =
			"\\src\\test\\java\\org\\geogebra\\web\\html5\\main\\inRegion.json";
	private AppWsimple app;

	@Test
	public void testLoadApp() {

		TestArticleElement articleElement = new TestArticleElement("prerelease", "simple");
		try {
			// Integration test so it is OK to use file
			Path currentRelativePath = Paths.get("");
			String basePath = currentRelativePath.toAbsolutePath().toString();
			String json = new String(Files.readAllBytes(Paths.get(basePath + jsonPath)));
			articleElement.attr("jsonFile", json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		app = AppMocker.mockAppletSimple(articleElement);
		assertTrue(((GeoBoolean)app.getKernel().lookupLabel("visible")).getBoolean());
	}

}
