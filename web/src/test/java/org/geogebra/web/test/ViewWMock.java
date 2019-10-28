package org.geogebra.web.test;

import java.util.Iterator;

import org.geogebra.common.move.ggtapi.models.json.JSONArray;
import org.geogebra.common.move.ggtapi.models.json.JSONException;
import org.geogebra.common.move.ggtapi.models.json.JSONObject;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.util.ViewW;

public class ViewWMock extends ViewW {
	/**
	 * @param app application
	 */
	public ViewWMock(AppW app) {
		super(app);
	}

	@Override
	public void processJSON(String encoded) {
		try {
			JSONArray array = new JSONArray(encoded);
			JSONObject content = array.getJSONObject(0);
			prepare(content.length());
			for (Iterator<String> it = content.keys(); it.hasNext(); ) {
				String key = it.next();
				putIntoArchiveContent(key, content.getString(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
