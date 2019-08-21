package org.geogebra.arbase;

import org.geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import org.geogebra.common.main.App;
import org.geogebra.common.main.Feature;
import org.geogebra.common.main.settings.EuclidianSettings3D;
import org.geogebra.common.util.DoubleUtil;

public class RatioManager {
    private double mARRatioAtStart;
    private EuclidianSettings3D mSettings3D;
    private String units = "cm";        // current units used for Ratio snack bar and ratio settings
    private String arRatioText = "1";   // current ratio used for Ratio snack bar and ratio settings

    public RatioManager(EuclidianSettings3D settings3D) {
        mSettings3D = settings3D;
    }

    public String getUnits() {
        return units;
    }

    public String getARRatioInString() {
        return arRatioText;
    }

    public void setARRatioAtStart(double arRatioAtStart) {
        mARRatioAtStart = arRatioAtStart;
        mSettings3D.setARRatio(arRatioAtStart);
    }

    public float getARRatioAtStart() {
        return (float) mARRatioAtStart;
    }

    public String getSnackBarText(App app) {
        double ratio = mSettings3D.getARRatio();
        String text;
        if (app.has(Feature.G3D_AR_RATIO_SETTINGS) &&
                mSettings3D.getARRatioMetricSystem() == EuclidianSettings3D.RATIO_UNIT_INCHES) {
            ratio = (double) Math.round(ratio * EuclidianSettings3D.FROM_CM_TO_INCH * 100d) / 100d;
            units = "inch";
        } else {
            if (ratio >= 100) {
                // round double for precision 3 in m
                ratio = (double) Math.round(ratio) / 100d;
                units = "m";
            } else if (ratio < 0.5 ) {
                // round double for precision 3 in mm
                ratio = (double) Math.round(ratio * 1000d) / 100d;
                units = "mm";
            } else {
                // round double for precision 3 in cm
                ratio = (double) Math.round(ratio * 100d) / 100d;
                units = "cm";
            }
        }
        text = getRatioMessage(ratio);
        return text;
    }

    private String getRatioMessage(double ratio) {
        if(DoubleUtil.isInteger(ratio)) {
            arRatioText = String.format("%d", (long) ratio);
        } else {
            arRatioText = String.format("%.4s", ratio);
        }
        return String.format("1 : %s %s", arRatioText, units);
    }
}
