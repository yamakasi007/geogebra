package org.geogebra.web.html5.euclidian;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.common.kernel.geos.GeoInlineText;
import org.geogebra.web.resources.JavaScriptInjector;
import org.geogebra.web.richtext.CarotaEditor;
import org.geogebra.web.richtext.Editor;
import org.geogebra.web.richtext.JavascriptBundle;

public class InlineTextControllerW implements InlineTextController {

	private static void initializeCarota() {
		JavaScriptInjector.inject(JavascriptBundle.INSTANCE.carotaJs());
	}

	private Panel parent;
	private Editor editor;

	public InlineTextControllerW(Panel parent) {
		initializeCarota();
		this.parent = parent;
	}

	@Override
	public void create() {
		editor = new CarotaEditor();
		editor.getWidget().getElement().getStyle().setZIndex(100);
		editor.getWidget().getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		parent.add(editor.getWidget());
		editor.focus();
	}

	@Override
	public void discard() {
		editor.getWidget().removeFromParent();
	}

	@Override
	public void setLocation(int x, int y) {
		editor.getWidget().getElement().getStyle().setLeft(x, Style.Unit.PX);
		editor.getWidget().getElement().getStyle().setTop(y, Style.Unit.PX);
	}

	@Override
	public void setWidth(int width) {
		editor.setWidth(width);
	}

	@Override
	public void setHeight(int height) {
		editor.setHeight(height);
	}

	@Override
	public void load(GeoInlineText inlineText) {

	}

	@Override
	public void persist(GeoInlineText inlineText) {

	}
}
