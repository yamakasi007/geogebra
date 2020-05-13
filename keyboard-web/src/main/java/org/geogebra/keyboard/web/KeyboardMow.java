package org.geogebra.keyboard.web;

import org.geogebra.keyboard.base.KeyboardFactory;

public class KeyboardMow extends KeyboardFactory {

	public KeyboardMow() {
		super();
		setSpecialSymbolsKeyboardFactory(new MowSpecialSymbolsKeyboardFactory());
	}
}
