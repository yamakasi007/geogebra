package org.geogebra.arbase;

import org.geogebra.common.util.DoubleUtil;
import org.geogebra.common.util.debug.Log;

public class MyTest {

    public MyTest() {
        Log.debug("D11bug: MyTest");

        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.011));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.09));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.043));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.43));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.5));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.54));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.63));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.68));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.98));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(0.92));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(1));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(2));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(3.02));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(4));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(5));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(6.49));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(6.62));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(9));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(10));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(11.2));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(13));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(14.4));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(14.8));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(15));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(14));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(19.99));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(20));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(20.001));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(21));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(21.35));
        debugStringArray("D11bug: ", ARRatioRoundedLogic(21.65));
    }

    private String[] ARRatioRoundedLogic(double input) {
        Log.debug("D11bug: ARRatioLogic With number: " + input);
        double arRatio = input;
        double higherRounding;
        double lowerRounding;
        double pot = DoubleUtil.getPowerOfTen(arRatio);
        double potedRatio = arRatio / pot;
        arRatio = potedRatio;
        Log.debug("D11bug: before round = " + arRatio);

        // add if == 1 then set higth ratio...
        if (arRatio <= 1) {
            higherRounding = arRatio == 1 ? 2 : 1;
            lowerRounding = 0.5;
        } else if (arRatio <= 2f) {
             higherRounding = arRatio == 2 ? 5 : 2;
                lowerRounding = 1;
        } else if (arRatio <= 5f) {
             higherRounding = arRatio == 5 ? 10 : 5;

//             higherRounding = 5;
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
        Log.debug("D11bug: potedRatio = " + potedRatio);
        Log.debug("D11bug: arRatio = " + arRatio);

//        potedRatio = Math.round(potedRatio);
        if (potedRatio == 1 || potedRatio == 2 || potedRatio == 5 ||
        arRatio
        == higherRounding || arRatio == lowerRounding) {
            return new String[]{stringFromDouble(higherRounding),
                    stringFromDouble(lowerRounding)};
        } else {
            return new String[]{stringFromDouble(higherRounding), stringFromDouble(arRatio),
                    stringFromDouble(lowerRounding)};
        }
    }

    private String stringFromDouble(Double value) {
        if(DoubleUtil.isInteger(value)) {
            return String.format("%d", value.longValue());
        } else {
            return String.format("%.3s", value);
        }
    }

    void debugStringArray(String debugMessage, String[] array) {
        for (int i = 0; i < array.length; i++) {
            Log.debug(debugMessage + " " + array[i]);
        }
    }
}
