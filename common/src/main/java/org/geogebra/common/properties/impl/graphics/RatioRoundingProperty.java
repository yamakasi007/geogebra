package org.geogebra.common.properties.impl.graphics;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.AbstractEnumerableProperty;

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
        super(localization, "RatioRounding");
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
        return 1;
    }

    private String[] getRatioClosestRounding() {
        double arRatio = Double.parseDouble(renderer.getARRatio());
        double rounding = round(arRatio,1);
        double higherRounding;
        double lowerRounding;

        // if X,X == Y,Y
        if (rounding == arRatio) {
            rounding = round(arRatio, 0);

            // if X,0 == Y,0
            if (rounding == arRatio) {
                higherRounding = arRatio + 1;
                lowerRounding = arRatio - 1;
                // if X,X == Y,Y
            } else if (rounding > arRatio) {
                higherRounding = rounding;
                lowerRounding = higherRounding;
                lowerRounding = lowerRounding - 1;
            } else {
                lowerRounding = rounding;
                higherRounding = lowerRounding;
                higherRounding = higherRounding + 1;
            }
            // if X,XX == Y,YY
        } else if (rounding > arRatio) {
            higherRounding = rounding;
            lowerRounding = higherRounding;
            lowerRounding = lowerRounding - 0.1;
        } else {
            lowerRounding = rounding;
            higherRounding = lowerRounding;
            higherRounding = higherRounding + 0.1;
        }

        roundingValues = new Double[]{higherRounding, arRatio, lowerRounding};

        return new String[]{Double.toString(roundingValues[0]), Double.toString(roundingValues[1]),
                Double.toString(roundingValues[2])};

    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
