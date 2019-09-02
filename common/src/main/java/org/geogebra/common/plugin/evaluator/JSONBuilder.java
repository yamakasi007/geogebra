package org.geogebra.common.plugin.evaluator;

import org.geogebra.common.move.ggtapi.models.json.JSONException;
import org.geogebra.common.move.ggtapi.models.json.JSONObject;

import javax.annotation.Nullable;

/***
 * Builds a JSON value from the parameters
 */
class JSONBuilder {

	/**
	 * Builds a JSON string from the parameters.
	 *
	 * @param flatString the string in GeoGebra format
	 * @param latexString the string in LaTeX format
	 * @param evalString the evaluated string, if the flatString is numeric,
	 *                      otherwise null
	 * @return JSON string from the parameters
	 */
	String buildJSONString(String flatString, String latexString,
								@Nullable  String evalString) {
		JSONObject object = new JSONObject();
		try {
			object.put("latex", latexString).put("flat", flatString);
			if (evalString != null) {
				object.put("eval", evalString);
			}
		} catch (JSONException exception) {
			// Can throw exception for numbers, can be ignored for Strings
		}
		return object.toString();
	}
}
