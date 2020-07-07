package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "PDFJS")
public class PdfDocumentLoadingTask extends Promise<PDFDocumentProxy> {
	public PdfDocumentLoadingTask(ConstructorParam parameters) {
		super(parameters);
	}
}
