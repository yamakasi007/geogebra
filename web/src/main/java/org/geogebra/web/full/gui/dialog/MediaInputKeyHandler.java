package org.geogebra.web.full.gui.dialog;

import org.geogebra.web.html5.gui.inputfield.AutoCompleteTextFieldW;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

/**
 * Input handlers of a textfield.
 * @author laszlo
 */
public class MediaInputKeyHandler implements KeyUpHandler, KeyDownHandler {

	private final ProcessInput input;

	/**
	 * @param processInput the process, input change code.
	 */
	public MediaInputKeyHandler(ProcessInput processInput) {
		this.input = processInput;
	}

	/**
	 *
	 * @param field to attach the handlers to.
	 */
	void attachTo(AutoCompleteTextFieldW field) {
		field.getTextBox().addKeyUpHandler(this);
		field.getTextBox().addKeyDownHandler(this);
		addNativeInputHandler(field.getInputElement());

	}

	private native void addNativeInputHandler(Element elem) /*-{
		var that = this;
		elem.addEventListener("input", function () {
			that.@org.geogebra.web.full.gui.dialog.MediaInputPanel::onInput()();
		});
	}-*/;

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (isEnterKey(event)) {
			input.processInput();
		}
	}

	private boolean isEnterKey(KeyCodeEvent<?> event) {
		return event.getNativeEvent()
				.getKeyCode() == KeyCodes.KEY_ENTER;
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (!isEnterKey(event)) {
			input.onInput();
		}
	}
}
