package org.geogebra.web.richtext;

import com.google.gwt.user.client.ui.Widget;

public interface Editor {

	Widget getWidget();

	void setWidth(int width);

	void setHeight(int height);

	String toString();

	void fromString(String state);

	boolean IsBold();

	void setBold(boolean bold);

	void insert(String text);

	void focus();

}
