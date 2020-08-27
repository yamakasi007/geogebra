package org.geogebra.web.html5.util.h5pviewer;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
final public class H5P {

	private H5P() {
		// use H5P.get() instead, may return null
	}

	@JsProperty(name = "h5p-standalone")
	public static native H5P get();
}
