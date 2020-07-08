package org.geogebra.web.html5.util.pdf;


import com.google.gwt.canvas.dom.client.Context2d;

import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "pdfjsLib", name = "renderContext")
public class PDFRenderContext {
	public PageViewPort viewPort;
	public Context2d context;

}
