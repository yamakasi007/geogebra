package org.geogebra.web.html5.gui.voiceInput.questResErr;

import org.geogebra.common.util.StringUtil;

public class AngleSizeQuestResErr implements QuestResErrInterface {

    private String response = "";

    @Override
    public int getID() {
        return QuestResErrConstants.ANGLESIZE;
    }

    @Override
    public String getQuestion() {
        return "Please give the angles size in degree.";
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public Double getResponseAsNumber() {
        return Double.valueOf(getResponse());
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String checkValidity() {
        if (StringUtil.isNumber(getResponse()) && getResponseAsNumber() > 0) {
            return "OK";
        } else if (!StringUtil.isNumber(getResponse())) {
            return QuestResErrConstants.ERR_MUST_BE_NUMBER;
        } else {
            return QuestResErrConstants.ERR_MUST_BE_POSITIVE;
        }
    }
}
