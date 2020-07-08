package org.geogebra.web.html5.util.pdf;


import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

@JsType(isNative = true)
public class PDFPageProxy {
	public native PageViewPort getViewport(Object o);
	public native void render(JsPropertyMap<Object> renderContext);
}
