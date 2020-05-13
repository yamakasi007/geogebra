package org.geogebra.keyboard.web;

import org.geogebra.common.gui.toolcategorization.AppType;
import org.geogebra.common.main.AppConfig;
import org.geogebra.common.main.AppKeyboardType;
import org.geogebra.common.main.Localization;

/**
 *
 */
public interface HasKeyboard {
	/**
	 * update keyboard height
	 */
	void updateKeyboardHeight();

	/**
	 * @return inner width
	 */
	double getInnerWidth();

	/**
	 * @return localization
	 */
	Localization getLocalization();

	/**
	 * @return true-if small keyboard needed
	 */
	boolean needsSmallKeyboard();

	/**
	 * update on keyboard close
	 */
	void updateViewSizes();

	/**
	 * @return the keyboard type based on the app, see {@link AppKeyboardType}
	 */
	AppKeyboardType getKeyboardType();
}
