package org.geogebra.common.kernel.geos.properties;

public enum TextAlignment {
    LEFT, CENTER, RIGHT;

    @Override
    public String toString() {
        switch (this) {
            case LEFT:
                return "left";
            case CENTER:
                return "center";
            case RIGHT:
                return "right";
            default:
            	return null;
        }
    }

    public static int alignmentToNumber(TextAlignment alignment) {
		switch (alignment) {
			case LEFT:
				return 0;
			case CENTER:
				return 1;
			default:
				return 2;
		}
	}

	public static TextAlignment numberToAlignment(int index) {
    	switch (index) {
			case 0:
				return LEFT;
			case 1:
				return CENTER;
			default:
				return RIGHT;
		}
	}
}
