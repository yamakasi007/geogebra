package org.geogebra.web.html5.main;

import org.geogebra.web.full.main.activity.EvaluatorActivity;

import com.google.gwt.core.client.JavaScriptObject;

import elemental2.core.Global;
import jsinterop.annotations.JsIgnore;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * API exporter for evaluator app
 */
public class EvaluatorExportedApi implements ExportedApi {

	private final EvaluatorActivity evaluatorActivity;

	private GgbAPIW ggbAPI;
	private ScriptManagerW scriptManager;

	/**
	 * @param evaluatorActivity
	 *            evaluator activity
	 */
	public EvaluatorExportedApi(EvaluatorActivity evaluatorActivity) {
		this.evaluatorActivity = evaluatorActivity;
	}

	@JsIgnore
	@Override
	public void setGgbAPI(GgbAPIW ggbAPI) {
		this.ggbAPI = ggbAPI;
	}

	@JsIgnore
	@Override
	public void setScriptManager(ScriptManagerW scriptManager) {
		this.scriptManager = scriptManager;
	}

	public Object getEditorState() {
		JavaScriptObject jsObject = JavaScriptObject.createObject();
		ScriptManagerW
				.addToJsObject(jsObject, evaluatorActivity.getEditorAPI().getEvaluatorValue());

		return jsObject;
	}

	public void evalLaTeX(String formula) {
		evaluatorActivity.getEditorAPI().evalLaTeX(formula);
	}

	public Object exportImage(JsPropertyMap<String> settings) {
		String type = Js.isTruthy(settings) ? settings.get("type") : null;
		return evaluatorActivity.exportImage(type);
	}

	public void setEditorState(Object state) {
		String stateString = "string".equals(Js.typeof(state)) ? Js.asString(state)
				: Global.JSON.stringify(state);
		evaluatorActivity.getEditorAPI().setEditorState(stateString);
	}

	public void registerClientListener(Object JSFunctionName) {
		ggbAPI.registerClientListener(scriptManager.getId(JSFunctionName));
	}
}
