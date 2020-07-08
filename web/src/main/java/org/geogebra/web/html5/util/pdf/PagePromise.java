package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class PagePromise<T> {

	@JsFunction
	public interface FunctionParam {
		void exec(PDFPageProxy page);
	}

	@JsFunction
	public interface ConstructorParam {
		void exec(FunctionParam resolve, FunctionParam reject);

	}

	@JsConstructor
	public PagePromise(ConstructorParam parameters) {
	}

	public native PagePromise<T> then(FunctionParam f);

	@JsMethod(name="catch")
	public native PagePromise<T> catch_(FunctionParam f);
}
