package org.geogebra.web.html5.util.pdf;

import elemental2.promise.Promise;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class PdfDocumentLoadingTask {
	public Promise<PDFDocumentProxy> promise;
}
