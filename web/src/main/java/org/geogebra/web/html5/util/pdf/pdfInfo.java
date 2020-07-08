package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class pdfInfo {
	@JsProperty
	public native int getNumPages();
}
