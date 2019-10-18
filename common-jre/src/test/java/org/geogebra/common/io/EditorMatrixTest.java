package org.geogebra.common.io;

import org.junit.Test;

public class EditorMatrixTest extends BaseEditorTest {
	private static final String matix3x3 = "{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}";

	@Test
	public void testMatrix() {
		type(matix3x3)
				.checkRaw("MathSequence[MathArray[MathSequence[" +
						"MathArray[MathSequence[1, ,,  , 2, ,,  , 3]], ,,  , " +
						"MathArray[MathSequence[4, ,,  , 5, ,,  , 6]], ,,  , " +
						"MathArray[MathSequence[7, ,,  , 8, ,,  , 9]]]]]");
	}

	@Test
	public void testCaretInitialPosition() {
		type(matix3x3).checkPath(0,0,0);
	}
}
