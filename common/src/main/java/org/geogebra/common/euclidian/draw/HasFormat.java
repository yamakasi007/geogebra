package org.geogebra.common.euclidian.draw;

public interface HasFormat {
	/**
	 * @param key
	 *            formatting option
	 * @param val
	 *            value (String, int or bool, depending on key)
	 */
	void format(String key, Object val);

	/**
	 * @param key formatting option name
	 * @param fallback fallback when not set / indeterminate
	 * @param <T> option type
	 * @return formatting option value or fallback
	 */
	<T> T getFormat(String key, T fallback);

	/**
	 * @return hyperlink of selected part, or at the end of text element if no selection
	 */
	String getHyperLinkURL();

	void setHyperlinkUrl(String url);

	String getHyperlinkRangeText();

	void insertHyperlink(String url, String text);

	/**
	 * @return list style of selected text
	 */
	String getListStyle();

	/**
	 * Switch the list type of selected text
	 * @param listType - numbered or bullet list
	 */
	void switchListTo(String listType);
}
