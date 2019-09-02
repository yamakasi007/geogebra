package org.geogebra.common.plugin.evaluator;

import org.geogebra.common.gui.editor.MathFieldCommon;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.SymbolicMode;
import org.geogebra.common.kernel.arithmetic.ValidExpression;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.commands.EvalInfo;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.parser.ParseException;
import org.geogebra.common.kernel.parser.Parser;

/**
 * API class for the Evaluator object.
 */
public class EvaluatorAPI {

	private MathFieldCommon mathFieldCommon;
	private AlgebraProcessor algebraProcessor;
	private Parser parser;
	private EvalInfo evalInfo;
	private JSONBuilder builder;

	/**
	 * Create a new Evaluator API
	 *
	 * @param kernel kernel for processing
	 * @param mathFieldCommon Math Field to create API for
	 */
	public EvaluatorAPI(Kernel kernel, MathFieldCommon mathFieldCommon) {
		this.mathFieldCommon = mathFieldCommon;
		this.algebraProcessor = kernel.getAlgebraProcessor();
		this.parser = kernel.getParser();
		this.evalInfo = createEvalInfo();
		this.builder = new JSONBuilder();
	}

	private EvalInfo createEvalInfo() {
		return new EvalInfo(false, false, false).withCAS(false)
				.withSliders(false).withSymbolicMode(SymbolicMode.NONE);
	}

	/**
	 * Get the value for the evaluator API.
	 *
	 * @return JSON string that contains values from the editor
	 */
	public String getEvaluatorValue() {
		String flatString = mathFieldCommon.getText();
		String latexString = mathFieldCommon.getLaTeX();
		String result = getEvalString(flatString);
		return builder.buildJSONString(flatString, latexString, result);
	}

	private String getEvalString(String flatString) {
		ValidExpression expression = parseString(flatString);
		if (expression == null || !expression.isNumberValue()) {
			return null;
		}
		return evaluateExpression(expression);
	}

	private ValidExpression parseString(String flatString) {
		try {
			return parser.parseGeoGebraExpression(flatString);
		} catch (ParseException e) {
			return null;
		}
	}

	private String evaluateExpression(ValidExpression expression) {
		try {
			GeoElementND[] elements = algebraProcessor.processAlgebraCommandNoExceptionHandling(
					expression, false, null, null, evalInfo);
			return processElements(elements);
		} catch (Exception e) {
			return null;
		}
	}

	private String processElements(GeoElementND[] elements) {
		if (elements == null || elements.length > 1) {
			return null;
		}
		GeoElementND element = elements[0];
		return element.toValueString(StringTemplate.defaultTemplate);
	}
}
