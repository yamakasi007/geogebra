package org.geogebra.web.html5.gui.voiceInput.command;

import org.geogebra.common.kernel.algos.AlgoAnglePoints;
import org.geogebra.common.kernel.algos.AlgoRotatePoint;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.web.html5.gui.voiceInput.questResErr.AngleSizeQuestResErr;
import org.geogebra.web.html5.gui.voiceInput.questResErr.QuestResErrInterface;
import org.geogebra.web.html5.gui.voiceInput.questResErr.XCoordQuestResErr;
import org.geogebra.web.html5.gui.voiceInput.questResErr.YCoordQuestResErr;
import org.geogebra.web.html5.main.AppW;

import java.util.ArrayList;

public class VoiceInputAngle implements VoiceInputCommandInterface {

    private ArrayList<QuestResErrInterface> questResList;

    public VoiceInputAngle() {
        initQuestList();
    }

    @Override
    public ArrayList<QuestResErrInterface> getQuestResList() {
        return questResList;
    }

    @Override
    public void initQuestList() {
        questResList = new ArrayList<>();
        questResList.add(new XCoordQuestResErr());
        questResList.add(new YCoordQuestResErr());
        questResList.add(new XCoordQuestResErr());
        questResList.add(new YCoordQuestResErr());
        questResList.add(new AngleSizeQuestResErr());
    }

    @Override
    public GeoElement createGeo(AppW appW, ArrayList<Double> inputList) {
        GeoPoint legPoint = new GeoPoint(appW.getKernel().getConstruction(),
                null, inputList.get(0), inputList.get(1), 1.0);
        GeoPoint vertexPoint = new GeoPoint(appW.getKernel().getConstruction(),
                null, inputList.get(2), inputList.get(3), 1.0);
        GeoNumeric angleValue = new GeoNumeric(appW.getKernel().getConstruction(),
                inputList.get(4));
        AlgoRotatePoint thirdPoint = new AlgoRotatePoint(appW.getKernel().getConstruction(),
                legPoint, angleValue, vertexPoint);
        AlgoAnglePoints angleAlgo = new AlgoAnglePoints(appW.getKernel().getConstruction(),
                legPoint, vertexPoint, (GeoPointND) thirdPoint.getOutput(0));
        return angleAlgo.getAngle();
    }

    @Override
    public String getStringRepresentation(ArrayList<Double> inputList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Angle with leg point coordinates ");
        sb.append(inputList.get(0));
        sb.append(" and ");
        sb.append(inputList.get(1));
        sb.append(", with vertex point coordinates ");
        sb.append(inputList.get(2));
        sb.append(" and ");
        sb.append(inputList.get(3));
        sb.append(" and with angle range ");
        sb.append(inputList.get(4));
        sb.append(" has been created.");
        return sb.toString();
    }
}