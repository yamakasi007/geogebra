package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "pdfjsLib")
public class PdfDocumentLoadingTask {
	public Promise<PDFDocumentProxy> promise;
}
