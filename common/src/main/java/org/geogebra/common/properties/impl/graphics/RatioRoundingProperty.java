package org.geogebra.common.properties.impl.graphics;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.AbstractEnumerableProperty;
import org.geogebra.common.util.DoubleUtil;

public class RatioRoundingProperty extends AbstractEnumerableProperty {

    private Renderer renderer;
    private double[] roundingValues;

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
            higherRounding = DoubleUtil.isEqual(arRatio, 1) ? 2 : 1;
            lowerRounding = 0.5;
        } else if (arRatio <= 2f) {
            higherRounding = DoubleUtil.isEqual(arRatio, 2) ? 5 : 2;
            lowerRounding = 1;
        } else if (arRatio <= 5f) {
            higherRounding = DoubleUtil.isEqual(arRatio, 5) ? 10 : 5;
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

        if (potedRatio == 1 || potedRatio == 2 || potedRatio == 5 || arRatio == higherRounding
                || arRatio == lowerRounding) {
            roundingValues = new double[]{higherRounding, lowerRounding};
            return new String[]{stringFromDouble(higherRounding),
                    stringFromDouble(lowerRounding)};
        } else {
            roundingValues = new double[]{higherRounding, arRatio, lowerRounding};
            return new String[]{stringFromDouble(higherRounding),
                    stringFromDouble(arRatio), stringFromDouble(lowerRounding)};
        }
    }

    private String stringFromDouble(Double value) {
        if(DoubleUtil.isInteger(value)) {
            return Integer.toString((int) Math.round(value));
        } else {
            return Double.toString(value);
        }
    }
}
