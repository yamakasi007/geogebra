package org.geogebra.common.plugin.evaluator;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.io.EditorTyper;
import org.geogebra.common.io.MathFieldCommon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the EvaluatorAPI
 */
public class EvaluatorAPITest extends BaseUnitTest {

	private EditorTyper typer;
	private EvaluatorAPI api;

	@Before
	public void setupTest() {
		MathFieldCommon mathField = new MathFieldCommon();
		api = new EvaluatorAPI(getKernel(), mathField.getInternal());
		typer = new EditorTyper(mathField);
	}

	@Test
	public void testGetEvaluatorValue() {
		typer.type("1/2");
		String value = api.getEvaluatorValue();
		Assert.assertEquals(
				"{\"latex\":\"{\\\\frac{1}{2}}\",\"flat\":\"(1)\\/(2)\",\"eval\":\"0.5\"}",
				value);
	}

	@Test
	public void testGetEvaluatorValueNonNumeric() {
		typer.type("GeoGebra");
		String value = api.getEvaluatorValue();
		Assert.assertEquals("{\"latex\":\"GeoGebra\",\"flat\":\"GeoGebra\",\"eval\":\"NaN\"}",
				value);
	}

	@Test
	public void testEmptyInput() {
		String value = api.getEvaluatorValue();
		Assert.assertEquals("{\"latex\":\"\\\\nbsp \",\"flat\":\"\",\"eval\":\"NaN\"}", value);
	}

	@Test
	public void testInvalidInput() {
		typer.type("1/");
		String value = api.getEvaluatorValue();
		Assert.assertEquals(
				"{\"latex\":\"{\\\\frac{1}{\\\\nbsp }}\",\"flat\":\"(1)\\/()\",\"eval\":\"NaN\"}",
				value);
	}
}
