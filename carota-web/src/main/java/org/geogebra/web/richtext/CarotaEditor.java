package org.geogebra.web.richtext;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class CarotaEditor implements Editor {

	private Widget widget;

	private JavaScriptObject editor;

	public CarotaEditor() {
		widget = createWidget();
	}

	private Widget createWidget() {
		HTML html = new HTML("<div></div>");
		editor = createEditorNative(html.getElement());
		return html;
	}

	private static native JavaScriptObject createEditorNative(Element div) /*-{
		return $wnd.carota.editor.create(div);
	}-*/;

	private static native void insertNative(JavaScriptObject editor, String text) /*-{
		return editor.insert(text);
	}-*/;

	@Override
	public void setWidth(int width) {
		widget.getElement().getStyle().setWidth(width, Style.Unit.PX);
	}

	@Override
	public void setHeight(int height) {
		widget.getElement().getStyle().setHeight(height, Style.Unit.PX);
	}

	@Override
	public void focus() {
		focusNative(editor);
	}

	private static native void focusNative(JavaScriptObject editor) /*-{
		editor.notifySelectionChanged(true);
	}-*/;

	@Override
	public void fromString(String state) {

	}

	@Override
	public boolean IsBold() {
		return false;
	}

	@Override
	public void setBold(boolean bold) {

	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	public void insert(String text) {
		insertNative(editor, text);
	}
}
