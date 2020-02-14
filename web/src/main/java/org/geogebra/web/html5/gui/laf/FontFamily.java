package org.geogebra.web.html5.gui.laf;

public enum FontFamily {
	ARIAL("Arial", "arial"),
	CALIBRI("Calibri", "Calibri"),
	CENTURY_GOTHIC("Century Gothic", "Century Gothic"),
	COMIC_SANS("Comic Sans", "Comic Sans MS"),
	COURIER("Courier", "Courier"),
	GEORGIA("Georgia", "Georgia"),
	DYSLEXIC("Open dyslexic mit Fibel a", "Open dyslexic mit Fibel a"),
	PALATINO("Palatino", "Palatino Linotype"),
	QUICKSAND("Qicksand", "Qicksand"),
	ROBOTO("Roboto", "Roboto"),
	SCHULBUCH_BAYERN("Schulbuch Bayern", "Schulbuch Bayern"),
	SF_MONO("SF Mono", "SF Mono"),
	SF_PRO("SF Pro", "SF Pro"),
	TIMES("Times", "Times"),
	TITILIUM("Titilium Web", "Titilium Web"),
	TREBUCHET("Trebuchet", "Trebuchet MS"),
	VERDANA("Verdana", "Verdana");
	private String displayName;
	private String cssName;

	FontFamily(String displayName, String cssName) {
		this.displayName = displayName;
		this.cssName = cssName;
	}

	public String displayName() {
		return displayName;
	}

	public String cssName() {
		return cssName;
	}
}
