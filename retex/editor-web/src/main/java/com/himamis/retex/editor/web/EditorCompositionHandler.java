package com.himamis.retex.editor.web;

import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventHandler;
import com.himamis.retex.editor.share.event.KeyEvent;
import com.himamis.retex.editor.share.model.Korean;
import com.himamis.retex.editor.share.util.JavaKeyCodes;

import jsinterop.base.Js;

/**
 * Composition handler for math input
 * 
 * @author Zbynek
 */
final class EditorCompositionHandler
		implements CompositionHandler, CompositionEndHandler, KeyDownHandler, KeyUpHandler {

	private MathFieldW editor;
	private boolean insertOnEnd = false;

	boolean backspace = false;

	/**
	 * @param editor
	 *            equation editor
	 */
	public EditorCompositionHandler(MathFieldW editor) {
		this.editor = editor;
	}

	public void attachTo(MyTextArea inputTextArea) {
		inputTextArea.addCompositionUpdateHandler(this);
		inputTextArea.addCompositionEndHandler(this);
		inputTextArea.addKeyDownHandler(this);
		inputTextArea.addKeyUpHandler(this);
	}

	@Override
	public void onCompositionUpdate(CompositionUpdateEvent event) {
		if (backspace) {
			return;
		}

		// this works fine for Korean as the editor has support for
		// combining Korean characters
		// but for eg Japanese probably will need to hook into
		// compositionstart & compositionend events as well

		// in Chrome typing fast gives \u3137\uB450
		// instead of \u3137\u315C
		// so flatten the result and send just the last character
		String data = Korean.flattenKorean(event.getData());
		// ^: fix for swedish
		if (!"^".equals(data) && data.length() > 0) {
			char inputChar = data.charAt(data.length() - 1);
			char lastChar = getLastChar(inputChar);
			if (Korean.isCompatibilityChar(lastChar)
					|| Korean.isSingleKoreanChar(lastChar)) {
				editor.insertString("" + lastChar);
			} else {
				insertOnEnd = true;
			}

		}
	}

	private char getLastChar(char inputChar) {
		String unmergedConsonant = Korean.unmergeDoubleCharacterToLeadTail(inputChar);
		if (unmergedConsonant.length() > 1) {
			return unmergedConsonant.charAt(1);
		}
		char vowel = Korean.unmergeDoubleVowels(inputChar);
		if (vowel != 0) {
			return vowel;
		}
		return Korean.convertToCompatibilityJamo(inputChar);
	}

	@Override
	public void onCompositionEnd(CompositionEndEvent event) {
		if (insertOnEnd) {
			// inserted string should only depend on `compositionend`
			// in Safari the data in `cmpositionupdate` is just Latin chars
			editor.insertString(event.getData());
			editor.getInternal().notifyAndUpdate(event.getData());
		}
	}

	private <T extends EventHandler> boolean isBackspace(KeyCodeEvent<T> event) {
		return "Backspace".equals(Js.asPropertyMap(event.getNativeEvent()).get("code"));
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (isBackspace(event)) {
			editor.getKeyListener().onKeyPressed(new KeyEvent(JavaKeyCodes.VK_BACK_SPACE));
			backspace = true;
		}
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (isBackspace(event)) {
			backspace = false;
		}
	}
}