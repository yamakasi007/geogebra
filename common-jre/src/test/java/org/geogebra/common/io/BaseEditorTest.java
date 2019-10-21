package org.geogebra.common.io;

import java.util.ArrayList;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.parser.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.himamis.retex.editor.share.controller.CursorController;
import com.himamis.retex.editor.share.controller.EditorState;
import com.himamis.retex.editor.share.editor.MathFieldInternal;
import com.himamis.retex.editor.share.io.latex.Parser;
import com.himamis.retex.editor.share.model.MathFormula;
import com.himamis.retex.editor.share.model.MathSequence;
import com.himamis.retex.editor.share.serializer.GeoGebraSerializer;
import com.himamis.retex.renderer.share.platform.FactoryProvider;

public class BaseEditorTest extends BaseUnitTest {
	private EditorChecker editorChecker = new EditorChecker();
	protected class EditorChecker {
		private MathFieldCommon mathField = new MathFieldCommon();
		private EditorTyper typer;

		// avoid synthetic access: can't be private
		protected EditorChecker() {
			typer = new EditorTyper(mathField);
		}

		public void checkAsciiMath(String output) {
			MathSequence rootComponent = getRootComponent();
			Assert.assertEquals(output,
					GeoGebraSerializer.serialize(rootComponent));
		}

		public void checkGGBMath(String output) {
			MathSequence rootComponent = getRootComponent();

			String exp = GeoGebraSerializer.serialize(rootComponent);

			try {
				ExpressionNode en = parse(exp);
				Assert.assertEquals(output, en.toString(StringTemplate.defaultTemplate));
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertEquals(output, "Exception: " + e.toString());
			}

		}

		public void checkRaw(String output) {
			MathSequence rootComponent = getRootComponent();
			Assert.assertEquals(output, rootComponent + "");
		}

		private MathSequence getRootComponent() {
			MathFieldInternal mathFieldInternal = mathField.getInternal();
			EditorState editorState = mathFieldInternal.getEditorState();
			return editorState.getRootComponent();
		}

		public EditorChecker type(String input) {
			typer.type(input);
			return this;
		}

		public EditorChecker typeKey(int key) {
			typer.typeKey(key);
			return this;
		}

		public EditorChecker repeatKey(int key, int count) {
			typer.repeatKey(key, count);
			return this;
		}

		public EditorChecker insert(String input) {
			typer.insert(input);
			return this;
		}

		public EditorChecker fromParser(String input) {
			Parser parser = new Parser(mathField.getMetaModel());
			MathFormula formula;
			try {
				formula = parser.parse(input);
				mathField.getInternal().setFormula(formula);
			} catch (Exception e) {
				Assert.fail("Problem parsing: " + input);
			}
			return this;
		}

		public EditorChecker checkPath(Integer... indexes) {
			MathFieldInternal mathFieldInternal = mathField.getInternal();
			mathField.requestViewFocus();
			mathFieldInternal.update();
			ArrayList<Integer> actual = CursorController.getPath(mathFieldInternal
					.getEditorState());
			Assert.assertArrayEquals(indexes, actual.toArray());
			return this;
		}
	}

	@BeforeClass
	public static void prepare() {
		if (FactoryProvider.getInstance() == null) {
			FactoryProvider.setInstance(new FactoryProviderCommon());
		}
	}

	protected void checkEditorInsert(String input, String output) {
		new EditorChecker().insert(input).checkAsciiMath(output);

	}

	public ExpressionNode parse(String exp) throws ParseException {
		return getApp().getKernel().getParser().parseExpression(exp);
	}


	protected EditorChecker type(String input) {
		return new EditorChecker().type(input);
	}
	protected EditorChecker fromParser(String input) {
		return new EditorChecker().fromParser(input);
	}

}
