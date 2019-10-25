package org.geogebra.web.html5.main;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.euclidian.EuclidianSimplePanelW;
import org.geogebra.web.test.AppMocker;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import com.himamis.retex.renderer.web.graphics.JLMContext2d;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({TextAreaElement.class, EuclidianSimplePanelW.class
	, JLMContext2d.class})
public class AppWSimpleLoadTest {
	@Test
	public void testLoadApp() {
		TestArticleElement articleElement = new TestArticleElement("prerelease", "simple");
		articleElement.attr("jsonFile", "inregion.json"); // not working
				AppWsimple app = AppMocker.mockAppletSimple(articleElement);
		Log.debug("JSOOON: " + app.getGgbApi().getFileJSON(false));
	}
}
