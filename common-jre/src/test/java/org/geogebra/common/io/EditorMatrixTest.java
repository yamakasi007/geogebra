package org.geogebra.common.io;

import org.junit.Test;

import com.himamis.retex.editor.share.util.JavaKeyCodes;

public class EditorMatrixTest extends BaseEditorTest {
	private static final String matix3x3 = "{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}";

	@Test
	public void testCaretInitialPosition() {
		fromParser(matix3x3).checkPath(0,0,0);
	}

	@Test
	public void testCaretShouldStayInsideOnTheLeft() {
		fromParser(matix3x3)
			.repeatKey(JavaKeyCodes.VK_RIGHT, 6)
			.repeatKey(JavaKeyCodes.VK_LEFT, 20).checkPath(0, 0, 0);
	}

	@Test
	public void testCaretShouldStayInsideOnTheRight() {
		fromParser(matix3x3).repeatKey(JavaKeyCodes.VK_RIGHT, 20).checkPath(1, 8, 0);
	}
}
