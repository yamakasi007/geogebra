package org.geogebra.main;

import static org.junit.Assert.assertEquals;

import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.AlgebraTest;
import org.geogebra.common.plugin.GgbAPI;
import org.geogebra.desktop.headless.AppDNoGui;
import org.junit.Before;
import org.junit.Test;

import com.himamis.retex.editor.share.util.Unicode;

public class APITest {
	private static AppDNoGui app;
	private static GgbAPI api;

	/**
	 * Initialize app.
	 */
	@Before
	public void setupApp() {
		app = AlgebraTest.createApp();
		api = app.getGgbApi();
	}

	@Test
	public void casEvalTest() {
		String assignResult = api.evalCommandCAS("$1:=a+a");
		assertEquals("2a", assignResult);
		String solveResult = api.evalGeoGebraCAS(
				"Solve[{ a=2, 12*sqrt(3)* a* b^2*exp(-3* b)-6*sqrt(3)* a* b*exp(-3* b)=0},{ a, b}]");
		assertEquals("{{a = 2, b = 0}, {a = 2, b = 1 / 2}}",
				solveResult);
		// OK in GUI, causes problems in the API - sent to Giac as
		// evalfa(ggbsort(normal(zeros((ggbtmpvart)^(2)=(4)*(ggbtmpvart),x))))
		String solveResult2 = api.evalGeoGebraCAS("Solutions[t^2 = 4t]");
		assertEquals("{0, 4}", solveResult2);
	}

	@Test
	public void casAssignmentTest() {
		api.evalCommand("$1:f(x)=x");
		assertEquals("f(x)=x", casInput(0));
		api.evalCommand("$2:g(x):=x");
		assertEquals("g(x):=x", casInput(1));
		api.evalCommand("$2:h(x)" + Unicode.ASSIGN_STRING + "x");
		assertEquals("h(x) := x", casInput(1));
		// overwrite existing cell
		api.evalCommand("$1:=f(x):=x+1");
		assertEquals("f(x):=x+1", casInput(0));
	}

	private String casInput(int i) {
		return app.getKernel().getConstruction()
				.getCasCell(i).getInput(StringTemplate.defaultTemplate);
	}

}
