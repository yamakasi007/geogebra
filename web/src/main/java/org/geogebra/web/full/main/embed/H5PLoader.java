package org.geogebra.web.full.main.embed;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.js.ResourcesInjector;
import org.geogebra.web.html5.util.ScriptLoadCallback;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;

/**
 * 
 * Loader for H5P
 *
 */
public class H5PLoader {
	public static final H5PLoader INSTANCE = new H5PLoader();
	private boolean loadingStarted;
	private static final String H5PJS = "../public/h5p/main.bundle.js";

	public void load() {
		if (loadingStarted) {
			return;
		}
		ScriptElement h5pInject = Document.get().createScriptElement();
		h5pInject.setSrc(H5PJS);
		loadingStarted = true;
		ResourcesInjector.loadJS(h5pInject, new ScriptLoadCallback() {

			@Override
			public void onLoad() {
				Log.debug("[H5P] main is loaded");
			}

			@Override
			public void onError() {
				Log.warn("Could not load H5P Viewer");
			}

			@Override
			public void cancel() {
				// no need to cancel
			}
		});
	}
}
