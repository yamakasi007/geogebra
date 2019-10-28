package org.geogebra.web.html5.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.geogebra.web.html5.euclidian.EuclidianSimplePanelW;
import org.geogebra.web.test.AppMocker;
import org.junit.Assert;
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
		AppWsimple app = AppMocker.mockAppletSimple(articleElement);
		Assert.assertEquals("B = (0, -0)", app.getKernel().lookupLabel("B").getAlgebraDescriptionDefault());

	}
}
