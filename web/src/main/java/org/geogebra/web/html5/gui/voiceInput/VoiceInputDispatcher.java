package org.geogebra.web.html5.gui.voiceInput;

import java.util.ArrayList;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.gui.voiceInput.command.VoiceInputAngle;
import org.geogebra.web.html5.gui.voiceInput.command.VoiceInputCircle;
import org.geogebra.web.html5.gui.voiceInput.command.VoiceInputCommandInterface;
import org.geogebra.web.html5.gui.voiceInput.command.VoiceInputPoint;
import org.geogebra.web.html5.gui.voiceInput.command.VoiceInputSegment;
import org.geogebra.web.html5.gui.voiceInput.questResErr.QuestResErrConstants;
import org.geogebra.web.html5.gui.voiceInput.questResErr.QuestResErrInterface;

/**
 * @author Csilla
 *
 *         handle different tasks
 *
 */
public class VoiceInputDispatcher {
	private ArrayList<QuestResErrInterface> questList;
	private VoiceInputCommandInterface currentCommand;

	/**
	 * command dispatcher
	 */
	public VoiceInputDispatcher() {
		questList = new ArrayList<>();
	}

	/**
	 * @return current task
	 */
	public VoiceInputCommandInterface getCurrentCommand() {
		return currentCommand;
	}

	/**
	 * @param currentCommand
	 *            current task
	 */
	public void setCurrentCommand(VoiceInputCommandInterface currentCommand) {
		this.currentCommand = currentCommand;
	}

	/**
	 * @param commandID
	 *            id of the task (e.g. create point)
	 */
	public void processCommand(int commandID) {
		questList.clear();
		switch (commandID) {
			case QuestResErrConstants.CREATE_POINT:
				setCurrentCommand(new VoiceInputPoint());
				break;
			case QuestResErrConstants.CREATE_SEGMENT:
				setCurrentCommand(new VoiceInputSegment());
				break;
			case QuestResErrConstants.CREATE_CIRCLE:
				setCurrentCommand(new VoiceInputCircle());
				break;
			case QuestResErrConstants.CREATE_ANGLE:
				setCurrentCommand(new VoiceInputAngle());
				break;
			default:
				// no command found
				Log.error("NO COMMAND FOUND IN INPUT");
				break;
		}
		questList = currentCommand.getQuestResList();
	}

	/**
	 * @return list of questions for current task
	 *
	 *         e.g. for point we need an x coordinate and y coordinate
	 */
	public ArrayList<QuestResErrInterface> getQuestList() {
		return questList;
	}

	/**
	 * @param command
	 *            tool name
	 * @return tool id
	 */
	public int getCommandID(String command) {
		switch (command) {
			case "Point":
			case "point":
				return QuestResErrConstants.CREATE_POINT;
			case "Segment":
			case "segment":
				return QuestResErrConstants.CREATE_SEGMENT;
			case "Circle":
			case "circle":
				return QuestResErrConstants.CREATE_CIRCLE;
			case "angle":
			case "Angle":
				return QuestResErrConstants.CREATE_ANGLE;

			default:
				return QuestResErrConstants.NOT_SUPPORTED;
		}
	}
}
