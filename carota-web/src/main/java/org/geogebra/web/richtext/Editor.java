package org.geogebra.web.richtext;

public interface Editor {

	String toString();

	void fromString(String state);

	boolean IsBold();

	void setBold(boolean bold);
}
