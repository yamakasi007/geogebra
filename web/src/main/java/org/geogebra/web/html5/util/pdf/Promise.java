package org.geogebra.web.html5.util.pdf;

import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Promise<T> {

	@JsFunction
	public interface FunctionParam {
		void exec(PDFDocumentProxy document);
	}

	@JsFunction
	public interface ConstructorParam {
		void exec(FunctionParam resolve, FunctionParam reject);

	}

	@JsConstructor
	public Promise(ConstructorParam parameters) {
	}

	public native Promise<T> then(FunctionParam f);

	@JsMethod(name="catch")
	public native Promise<T> catch_(FunctionParam f);
}
