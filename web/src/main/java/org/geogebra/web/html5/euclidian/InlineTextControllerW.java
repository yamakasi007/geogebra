package org.geogebra.web.html5.euclidian;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.web.richtext.impl.CarotaEditor;
import org.geogebra.web.richtext.Editor;

/**
 * Web implementation of the inline text controller.
 */
public class InlineTextControllerW implements InlineTextController {

	private Panel parent;
	private Editor editor;
	private Style style;

	public InlineTextControllerW(Panel parent) {
		this.parent = parent;
	}

	@Override
	public void create() {
		editor = new CarotaEditor();
		Widget widget = editor.getWidget();
		style = widget.getElement().getStyle();
		style.setZIndex(100);
		style.setPosition(Style.Position.ABSOLUTE);
		parent.add(editor.getWidget());
		editor.focus();
	}

	@Override
	public void discard() {
		editor.getWidget().removeFromParent();
	}

	@Override
	public void setLocation(int x, int y) {
		style.setLeft(x, Style.Unit.PX);
		style.setTop(y, Style.Unit.PX);
	}

	@Override
	public void setWidth(int width) {
		style.setWidth(width, Style.Unit.PX);
	}

	@Override
	public void setHeight(int height) {
		style.setHeight(height, Style.Unit.PX);
	}
}
