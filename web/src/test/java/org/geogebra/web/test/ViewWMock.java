package org.geogebra.web.test;

import java.util.Iterator;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.util.ViewW;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewWMock extends ViewW {
	/**
	 * @param app application
	 */
	public ViewWMock(AppW app) {
		super(app);
	}

	@Override
	public void processJSON(String encoded) {
		JSONArray array = new JSONArray(encoded);
		JSONObject content = (JSONObject) array.get(0);
		prepare(content.length());
		for (Iterator<String> it = content.keys(); it.hasNext(); ) {
			String key = it.next();
			putIntoArchiveContent(key, content.getString(key));
		}
		Log.debug("ENCODED JSON: " + encoded);
	}
}
