package org.geogebra.web.richtext;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Inline text editor based on Carota.
 */
public class CarotaEditor implements Editor {

	private Widget widget;
	private JavaScriptObject editor;

	private static native JavaScriptObject createEditorNative(Element div) /*-{
		return $wnd.carota.editor.create(div);
	}-*/;

	private static native void focusNative(JavaScriptObject editor) /*-{
		editor.notifySelectionChanged(true);
	}-*/;

	/**
	 * Create a new instance of Carota editor.
	 */
	public CarotaEditor() {
		ScriptInjector.fromString(JavascriptBundle.INSTANCE.carotaJs().getText()).setRemoveTag()
		widget = createWidget();
	}

	private Widget createWidget() {
		HTML html = new HTML("<div></div>");
		editor = createEditorNative(html.getElement());
		return html;
	}

	@Override
	public void focus() {
		focusNative(editor);
	}

	@Override
	public Widget getWidget() {
		return widget;
	}
}
