package org.geogebra.common.euclidian.text;

import org.geogebra.common.kernel.geos.GeoInlineText;

public interface InlineTextController {

	void create();

	void discard();


	void setLocation(int x, int y);

	void setWidth(int width);

	void setHeight(int height);


	void load(GeoInlineText inlineText);

	void persist(GeoInlineText inlineText);
}
