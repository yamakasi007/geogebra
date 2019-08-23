package org.geogebra.common.properties.impl.graphics;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.AbstractEnumerableProperty;
import org.geogebra.common.util.DoubleUtil;

public class RatioRoundingProperty extends AbstractEnumerableProperty {

    private Renderer renderer;
    private Double[] roundingValues;

    /**
     * Constructs an ratio rounding property.
     *
     * @param renderer     renderer
     * @param localization localization
     */
    public RatioRoundingProperty(Renderer renderer, Localization localization) {
        super(localization, "Rounding");
        this.renderer = renderer;
        setValuesAndLocalize(getRatioClosestRounding());
    }

    @Override
    protected void setValueSafe(String value, int index) {
        renderer.setARRatio(roundingValues[index]);
    }

    @Override
    public int getIndex() {
        setValues(getRatioClosestRounding());
        setValuesAndLocalize(getRatioClosestRounding());
        if (roundingValues.length == 3) {
            return 1;
        } else {
            return 0;
        }
    }

    private String[] getRatioClosestRounding() {
        double arRatio = Double.parseDouble(renderer.getARRatio());
        double higherRounding;
        double lowerRounding;
        double pot = DoubleUtil.getPowerOfTen(arRatio);
        double potedRatio = arRatio / pot;
        arRatio = potedRatio;

        if (arRatio <= 1) {
            higherRounding = arRatio == 1 ? 2 : 1;
            lowerRounding = 0.5;
        } else if (arRatio <= 2f) {
            higherRounding = arRatio == 2 ? 5 : 2;
            lowerRounding = 1;
        } else if (arRatio <= 5f) {
            higherRounding = arRatio == 5 ? 10 : 5;
            lowerRounding = 2;
        } else {
            higherRounding = 10;
            lowerRounding = 5;
        }
        if (pot < 1) {
            arRatio = Math.round(arRatio);
            arRatio *= pot;
        } else {
            arRatio *= pot;
            arRatio = Math.round(arRatio);
        }

        higherRounding = higherRounding * pot;
        lowerRounding = lowerRounding * pot;

        if (potedRatio == 1 || potedRatio == 2 || potedRatio == 5 ||
                arRatio == higherRounding || arRatio == lowerRounding) {
            roundingValues = new Double[]{higherRounding, lowerRounding};
            return new String[]{stringFromDouble(roundingValues[0]),
                    stringFromDouble(roundingValues[1])};
        } else {
            // when using unit inches, there can be the case when arRatio is very close to 0
            if (arRatio < 1) {
                roundingValues = new Double[]{1.0, 0.5};
                return new String[]{stringFromDouble(roundingValues[0]),
                        stringFromDouble(roundingValues[1])};
            } else {
                roundingValues = new Double[]{higherRounding, arRatio, lowerRounding};
                return new String[]{stringFromDouble(roundingValues[0]),
                        stringFromDouble(roundingValues[1]), stringFromDouble(roundingValues[2])};
            }
        }
    }

    private String stringFromDouble(Double value) {
        if(DoubleUtil.isInteger(value)) {
            return Integer.toString(value.intValue());
        } else {
            return Double.toString(value);
        }
    }
}
