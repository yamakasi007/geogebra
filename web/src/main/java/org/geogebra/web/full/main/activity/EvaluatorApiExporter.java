package org.geogebra.web.full.main.activity;

import org.geogebra.web.html5.main.ApiExporter;
import org.geogebra.web.html5.main.GgbAPIW;

import com.google.gwt.core.client.JavaScriptObject;

public class EvaluatorApiExporter extends ApiExporter {

	private EvaluatorActivity evaluatorActivity;

	public EvaluatorApiExporter(EvaluatorActivity evaluatorActivity) {
		this.evaluatorActivity = evaluatorActivity;
	}

	@Override
	protected void addFunctions(JavaScriptObject api, GgbAPIW ggbAPI) {
		addEditorState(api);
	}

	private String getEditorState() {
		return evaluatorActivity.getEditorAPI().getEvaluatorValue();
	}

	private native void addEditorState(JavaScriptObject api) /*-{
		var that = this;
		api.getEditorState = function() {
			return that.@org.geogebra.web.full.main.activity.EvaluatorApiExporter::getEditorState()();
		};
	}-*/;

	@Override
	protected void addListenerFunctions(JavaScriptObject api, GgbAPIW ggbAPI,
			JavaScriptObject getId) {
		addClientListener(api, ggbAPI, getId);
	}

}
