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
	public void testCaretShouldStayInsideOnHomeInitially() {
		fromParser(matix3x3).typeKey(JavaKeyCodes.VK_HOME).checkPath(0,0,0);
	}

	@Test
	public void testCaretShouldStayInsideOnHome() {
		fromParser(matix3x3)
				.repeatKey(JavaKeyCodes.VK_RIGHT, 6)
				.typeKey(JavaKeyCodes.VK_HOME).checkPath(0, 0, 0);
	}
		@Test
	public void testCaretShouldStayInsideOnEnd() {
		fromParser(matix3x3).typeKey(JavaKeyCodes.VK_END).checkPath(1, 8, 0);
	}

	@Test
	public void testCaretShouldStayInsideOnLeftArrow() {
		fromParser(matix3x3)
			.repeatKey(JavaKeyCodes.VK_RIGHT, 6)
			.repeatKey(JavaKeyCodes.VK_LEFT, 20).checkPath(0, 0, 0);
	}

	@Test
	public void testCaretShouldStayInsideOnRightArrow() {
		fromParser(matix3x3).repeatKey(JavaKeyCodes.VK_RIGHT, 20).checkPath(1, 8, 0);
	}

	@Test
	public void testCaretShouldStayInsideOnUpArrow() {
		fromParser(matix3x3)
				.repeatKey(JavaKeyCodes.VK_DOWN, 2)
				.repeatKey(JavaKeyCodes.VK_UP, 20).checkPath(0, 0, 0);
	}

	@Test
	public void testCaretShouldStayInsideOnDownArrow() {
		fromParser(matix3x3).repeatKey(JavaKeyCodes.VK_DOWN, 20).checkPath(0, 6, 0);
	}
}
