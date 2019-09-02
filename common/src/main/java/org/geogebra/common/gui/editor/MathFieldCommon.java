package org.geogebra.common.gui.editor;

import com.himamis.retex.editor.share.editor.MathFieldInternal;
import com.himamis.retex.editor.share.model.MathFormula;
import com.himamis.retex.editor.share.serializer.GeoGebraSerializer;
import com.himamis.retex.editor.share.serializer.Serializer;
import com.himamis.retex.editor.share.serializer.TeXSerializer;

public class MathFieldCommon {

	private MathFieldInternal mathFieldInternal;
	private Serializer flatSerializer;
	private Serializer latexSerializer;

	public MathFieldCommon(MathFieldInternal mathFieldInternal) {
		this.mathFieldInternal = mathFieldInternal;
		this.flatSerializer = new GeoGebraSerializer();
		this.latexSerializer = new TeXSerializer();
	}

	public String getText() {
		return flatSerializer.serialize(getFormula());
	}

	public String getLaTeX() {
		return latexSerializer.serialize(getFormula());
	}

	private MathFormula getFormula() {
		return mathFieldInternal.getFormula();
	}
}
