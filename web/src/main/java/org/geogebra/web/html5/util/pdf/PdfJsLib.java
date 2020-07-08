package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name="pdfjsLib")
public class PdfJsLib {
	public static native PdfDocumentLoadingTask getDocument(String src);
}
